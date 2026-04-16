package ci.nsu.mobile.main.models

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
) {
    val formattedDate: String
        get() = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(calculationDate))
}