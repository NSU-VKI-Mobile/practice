package ci.nsu.moble.main.model

data class ShoppingItem(
    val id: Int,
    val name: String,
    val isBought: Boolean = false
)