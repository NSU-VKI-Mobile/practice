package ci.nsu.mobile.main.ui

data class ShoppingItem(
    val id: Int,
    val name: String,
    val isBought: Boolean = false
)

data class ShoppingListUiState(
    val items: List<ShoppingItem> = emptyList(),
    val newItemText: String = ""
)