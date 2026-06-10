package ci.nsu.mobile.main.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Int,
    val initialAmount: Double,
    val months: Int,
    val rate: Double,
    val topUp: Double,
    val finalAmount: Double,
    val interest: Double,
    val date: Long
)