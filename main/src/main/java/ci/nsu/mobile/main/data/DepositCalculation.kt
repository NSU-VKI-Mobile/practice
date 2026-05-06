package ci.nsu.mobile.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val months: Int,
    val rate: Double,
    val topUp: Double?,
    val result: Double,
    val date: Long
)