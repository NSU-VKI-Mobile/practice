package ci.nsu.moble.main.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
) {
    fun getFormattedDate(): String {
        val date = Date(calculationDate)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}