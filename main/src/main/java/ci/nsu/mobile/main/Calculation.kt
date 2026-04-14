package ci.nsu.mobile.main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class Calculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startAmount: Double,
    val termMonths: Int,
    val interestRate: Double,
    val monthlyDeposit: Double,
    val totalAmount: Double,
    val totalProfit: Double,
    val date: Long
)