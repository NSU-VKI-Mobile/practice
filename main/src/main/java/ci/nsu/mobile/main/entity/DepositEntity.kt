package ci.nsu.mobile.main.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Long = 0L,
    val startAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val profit: Double,
    val date: Long
)