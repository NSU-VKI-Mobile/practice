package ci.nsu.mobile.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShoppingItem(
    val id: Int,
    val name: String,
    val isBought: Boolean = false
)

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val newItemText: String = ""
)

class ShoppingViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_NEW_ITEM_TEXT = "new_item_text"
        private const val KEY_ITEMS = "items"
        private const val KEY_NEXT_ID = "next_id"
    }

    private val _uiState = MutableStateFlow(
        ShoppingListUiState(
            items = restoreItems(),
            newItemText = savedStateHandle[KEY_NEW_ITEM_TEXT] ?: ""
        )
    )

    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    private var nextId: Int
        get() = savedStateHandle[KEY_NEXT_ID]
            ?: (_uiState.value.items.maxOfOrNull { it.id }?.plus(1) ?: 1)
        set(value) {
            savedStateHandle[KEY_NEXT_ID] = value
        }

    fun onNewItemTextChanged(text: String) {
        savedStateHandle[KEY_NEW_ITEM_TEXT] = text
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val currentText = _uiState.value.newItemText.trim()
        if (currentText.isBlank()) return

        _uiState.update { currentState ->
            val newItem = ShoppingItem(
                id = nextId,
                name = currentText
            )
            nextId += 1

            val updatedItems = currentState.items + newItem
            saveItems(updatedItems)
            savedStateHandle[KEY_NEW_ITEM_TEXT] = ""

            currentState.copy(
                items = updatedItems,
                newItemText = ""
            )
        }
    }

    fun toggleItemBought(itemId: Int) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.map { item ->
                if (item.id == itemId) {
                    item.copy(isBought = !item.isBought)
                } else {
                    item
                }
            }
            saveItems(updatedItems)
            currentState.copy(items = updatedItems)
        }
    }

    fun deleteItem(itemId: Int) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.filterNot { it.id == itemId }
            saveItems(updatedItems)
            currentState.copy(items = updatedItems)
        }
    }

    private fun saveItems(items: List<ShoppingItem>) {
        val encoded = items.joinToString("||") { item ->
            val safeName = item.name.replace("||", "").replace(";;", "")
            ";;;;"
        }
        savedStateHandle[KEY_ITEMS] = encoded
    }

    private fun restoreItems(): List<ShoppingItem> {
        val encoded: String = savedStateHandle[KEY_ITEMS] ?: return emptyList()
        if (encoded.isBlank()) return emptyList()

        return encoded.split("||").mapNotNull { rawItem ->
            val parts = rawItem.split(";;")
            if (parts.size != 3) return@mapNotNull null

            val id = parts[0].toIntOrNull() ?: return@mapNotNull null
            val name = parts[1]
            val isBought = parts[2].toBooleanStrictOrNull() ?: false

            ShoppingItem(
                id = id,
                name = name,
                isBought = isBought
            )
        }
    }
}
