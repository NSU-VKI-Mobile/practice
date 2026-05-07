package ci.nsu.mobile.main.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "DepCal")
data class DepCalcs(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,       // Double, не Double?
    val interestRate: Double,        // Double, не Double?
    val periodMonths: Int,           // Int
    val monthlyTopUp: Double,        // Double, не Double?
    val finalAmount: Double,         // Double, не Double?
    val interestEarned: Double,      // Double, не Double?
    val calculationDate: String = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())  // String
)