package ci.nsu.mobile.main.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_lists")
data class ShoppingList(
    @PrimaryKey
    val date: String,
    val items: List<String>
)