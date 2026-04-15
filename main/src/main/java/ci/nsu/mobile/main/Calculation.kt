package ci.nsu.mobile.main

import androidx.room.Entity
import androidx.room.PrimaryKey
// структура данных для сохранения в бд
@Entity(tableName = "calculations")// аннотация Room это таблица в бд
data class Calculation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startAmount: Double,//стартовый взнос
    val termMonths: Int,// срок в мес
    val interestRate: Double,// процентная ставка
    val monthlyDeposit: Double,//еж пополнение
    val totalAmount: Double,// общ доход
    val totalProfit: Double,// дата расчета
    val date: Long
)