package ci.nsu.mobile.main.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Float,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Float,
    val finalAmount: Float,
    val interestEarned: Float,
    val calculationDate: Long
)