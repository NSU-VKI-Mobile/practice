package ci.nsu.mobile.main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true) //номер будет присваиваться автоматически
    val id: Long = 0,
    val initialAmount: Double, //стартовый взнос
    val periodMonths: Int,     //срок в месяцах
    val interestRate: Double,  //процентная ставка
    val monthlyTopUp: Double,  //ежемесячное пополнение
    val finalAmount: Double,   //итоговая сумма
    val interestEarned: Double, //начисленные проценты
    val calculationDate: Long = System.currentTimeMillis()  //дата расчёта
    //: Long — тип для хранения времени (миллисекунды с 1970 года)
    //= System.currentTimeMillis() — если не указать дату, подставится текущее время
)