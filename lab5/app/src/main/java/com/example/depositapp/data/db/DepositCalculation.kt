package com.example.depositapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity — говорит Room: "этот класс = таблица в базе данных"
// tableName — имя таблицы в SQLite
@Entity(tableName = "deposit_calculations")
data class DepositCalculation(

    // @PrimaryKey — первичный ключ, уникальный идентификатор каждой записи
    // autoGenerate = true — база сама придумывает id (1, 2, 3...)
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Стартовый взнос, например 100000.0
    val initialAmount: Double,

    // Срок вклада в месяцах, например 12
    val periodMonths: Int,

    // Процентная ставка, например 10.0
    val interestRate: Double,

    // Ежемесячное пополнение — может быть null (необязательное поле)
    val monthlyTopUp: Double?,

    // Итоговая сумма после начисления процентов
    val finalAmount: Double,

    // Сколько процентов начислено = finalAmount - initialAmount - все пополнения
    val interestEarned: Double,

    // Дата сохранения — храним как число (миллисекунды с 1970 года)
    // System.currentTimeMillis() даёт это число
    val calculationDate: Long
)
