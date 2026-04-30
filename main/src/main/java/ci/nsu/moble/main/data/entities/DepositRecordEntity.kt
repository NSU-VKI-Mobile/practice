package ci.nsu.moble.main.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class DepositRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val initialAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyAdd: Double,
    val finalAmount: Double,
    val profit: Double,
    val date: Long = System.currentTimeMillis()
)