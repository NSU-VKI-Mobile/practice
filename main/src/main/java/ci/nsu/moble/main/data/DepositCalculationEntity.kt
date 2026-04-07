package ci.nsu.moble.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: String,
    val initialAmount: Double,
    val months: Int,
    val ratePercent: Int,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestAmount: Double
)