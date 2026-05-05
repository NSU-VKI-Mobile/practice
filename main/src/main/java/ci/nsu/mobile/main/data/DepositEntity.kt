package ci.nsu.mobile.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val initialAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val dateTimestamp: Long = System.currentTimeMillis()
)