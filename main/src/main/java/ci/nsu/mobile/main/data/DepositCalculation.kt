package ci.nsu.mobile.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val depositName: String,
    val calculationDate: Long // timestamp
)