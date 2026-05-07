package ci.nsu.mobile.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposits")
data class Deposit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyAddition: Double,
    val totalAmount: Double,
    val earnedInterest: Double,
    val dateTime: String
)