package ci.nsu.mobile.main

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

class ShoppingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    fun onNewItemTextChanged(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val currentText = _uiState.value.newItemText.trim()
        if (currentText.isBlank()) return
        val alreadyExists = _uiState.value.items.any {
            it.name.equals(currentText, ignoreCase = true)
        }
        if (alreadyExists) {
            return
        }
        _uiState.update { currentState ->
            val newItem = ShoppingItem(
                id = currentState.items.size + 1,
                name = currentText
            )
            currentState.copy(
                items = currentState.items + newItem,
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
            currentState.copy(
                items = sortItems(updatedItems)
            )
        }
    }

    fun deleteItem(itemId: Int) {
        _uiState.update { currentState ->
            val updatedItems = currentState.items.filterNot { it.id == itemId }
            currentState.copy(items = updatedItems)
        }
    }

    private fun sortItems(items: List<ShoppingItem>): List<ShoppingItem> {
        val sortone = items.sortedBy{it.name}
        val sorttwo = sortone.sortedBy{ it.isBought }
        return sorttwo
    }
}
