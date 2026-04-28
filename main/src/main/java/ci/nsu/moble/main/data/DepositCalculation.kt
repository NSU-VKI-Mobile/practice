package ci.nsu.moble.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,   // 0.0 если не указано
    val finalAmount: Double,
    val interestEarned: Double,
    val date: Long              // timestamp
)