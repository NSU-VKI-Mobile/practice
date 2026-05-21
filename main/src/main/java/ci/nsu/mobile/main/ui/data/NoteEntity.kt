package ci.nsu.mobile.main.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String, // храним как строку в формате yyyy-MM-dd
    val text: String,
    val isCompleted: Boolean = false
)