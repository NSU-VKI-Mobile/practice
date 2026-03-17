package ci.nsu.moble.main.ui.main

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

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    fun onNewItemTextChanged(text: String) {
        _uiState.update { it.copy(newItemText = text) }
    }

    fun addItem() {
        val text = _uiState.value.newItemText
        if (text.isNotBlank()) {
            _uiState.update { state ->
                val exists = state.items.any { it.name.equals(text, ignoreCase = true) }

                if (exists) {
                    state
                } else {
                    val newItem = ShoppingItem(
                        id = state.items.size + 1,
                        name = text
                    )
                    state.copy(
                        items = state.items + newItem,
                        newItemText = ""
                    )
                }
            }
        }
    }

    fun toggleItemBought(id: Int) {
        _uiState.update { state ->
            state.copy(
                items = state.items.map {
                    if (it.id == id) it.copy(isBought = !it.isBought)
                    else it
                }
            )
        }
    }

    fun deleteItem(id: Int) {
        _uiState.update { state ->
            state.copy(items = state.items.filter { it.id != id })
        }
    }
}