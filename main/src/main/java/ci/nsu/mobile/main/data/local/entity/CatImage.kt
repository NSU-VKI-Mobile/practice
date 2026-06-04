package ci.nsu.mobile.main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cat_images")
data class CatImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val url: String,
    val date: Date
)