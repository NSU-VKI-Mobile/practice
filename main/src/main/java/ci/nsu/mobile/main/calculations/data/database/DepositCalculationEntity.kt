package ci.nsu.mobile.main.calculations.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "deposit_calculations",
    indices = [Index(value = ["userId"])]  //представления для удобства
)
data class DepositCalculationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,  // owner
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long        // timestamp
)