package ci.nsu.mobile.depositcalculator.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val termMonths: Int,
    val interestRate: Double,
    val capitalization: Boolean,
    val finalAmount: Double,
    val profit: Double,
    val timestamp: Long = System.currentTimeMillis()
)

