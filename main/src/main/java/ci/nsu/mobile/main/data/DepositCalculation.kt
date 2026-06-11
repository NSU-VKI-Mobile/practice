package ci.nsu.mobile.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Одна запись в таблице истории расчётов
@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyDeposit: Double,
    val finalAmount: Double,
    val earnedInterest: Double,
    val createdAt: Long = System.currentTimeMillis()
)