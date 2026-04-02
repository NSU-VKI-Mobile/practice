package ci.nsu.moble.main

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
        val currentText = _uiState.value.newItemText.lowercase()
        val isNotPresent = _uiState.value.items.none {it.name==currentText}
        if (currentText.isNotBlank() && isNotPresent) {
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
            currentState.copy(items = updatedItems)
        }
    }


    fun toggleAllItems() {
        val allSelected = _uiState.value.items.all { it.isBought }

        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map { item ->
                item.copy(isBought = !allSelected)
            }
        )
    }

    fun deleteItem() {
        _uiState.update { currentState ->
            currentState.copy(items = currentState.items.filter { item -> item.isBought == false })
        }
    }
}