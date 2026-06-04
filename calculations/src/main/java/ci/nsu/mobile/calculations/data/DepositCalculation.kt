package ci.nsu.mobile.calculations.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,       // Стартовый взнос
    val periodMonths: Int,           // Срок в месяцах
    val interestRate: Double,        // Процентная ставка
    val monthlyTopUp: Double?,       // Ежемесячное пополнение (может быть null)
    val finalAmount: Double,         // Итоговая сумма
    val interestEarned: Double,      // Начисленные проценты
    val calculationDate: Long        // Дата расчёта (timestamp)
)