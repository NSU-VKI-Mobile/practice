package ci.nsu.mobile.main.data.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val months: Int,
    val rate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val profit: Double,
    val date: Long
)