package ci.nsu.mobile.calculations.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)