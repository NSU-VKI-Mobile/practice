import androidx.lifecycle.ViewModel
import ci.nsu.moble.main.ShoppingItem
import ci.nsu.moble.main.ShoppingListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ShoppingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    fun onNewItemTextChanged(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val currentText = _uiState.value.newItemText
        if (currentText.isNotBlank()) {
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

    fun deleteItem(itemId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                items = currentState.items.filter{it.id != itemId}
            )
        }
    }
}