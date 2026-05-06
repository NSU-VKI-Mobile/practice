package data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="deposit_calculations")
data class DepositCalculations(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,
    val termMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long,
    val currency: String
)
