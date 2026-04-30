package ci.nsu.mobile.main.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

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
    fun getFormattedDate(): String {
        val date = Date(calculationDate)
        return android.text.format.DateFormat.format("dd.MM.yyyy HH:mm:ss", date).toString()
    }
}