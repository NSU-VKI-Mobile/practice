package ci.nsu.moble.main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val initialAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyAdd: Double,
    val finalAmount: Double,
    val profit: Double,
    val date: String
)