package ci.nsu.mobile.main.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val initialAmount: Double,      // Стартовый взнос
    val periodMonths: Int,           // Срок в месяцах
    val interestRate: Double,        // Процентная ставка
    val monthlyTopUp: Double?,       // Ежемесячное пополнение
    val finalAmount: Double,         // Итоговая сумма
    val interestEarned: Double,      // Начисленные проценты
    val calculationDate: Long = System.currentTimeMillis()  // Дата расчёта
) {
    fun getFormattedDate(): String {
        val date = Date(calculationDate)
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}