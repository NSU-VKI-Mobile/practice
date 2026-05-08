package com.example.depositapp.domain

// Модель данных для передачи между экранами
// Это НЕ таблица базы данных — это просто контейнер для данных в процессе ввода
// Пользователь заполняет по шагам: шаг 1 → шаг 2 → результат
data class DepositInput(
    val initialAmount: Double = 0.0,   // Стартовый взнос
    val periodMonths: Int = 0,         // Срок в месяцах
    val interestRate: Double = 0.0,    // Процентная ставка
    val monthlyTopUp: Double = 0.0     // Ежемесячное пополнение (0 = нет)
)

// Вычисление итогов вклада
// Формула с ежемесячным пополнением:
// Каждый месяц: остаток = (остаток + пополнение) * (1 + ставка/12/100)
fun calculateDeposit(input: DepositInput): Pair<Double, Double> {
    var balance = input.initialAmount
    val monthlyRate = input.interestRate / 100.0 / 12.0

    repeat(input.periodMonths) {
        balance += input.monthlyTopUp          // добавляем пополнение
        balance += balance * monthlyRate       // начисляем проценты
    }

    val totalDeposited = input.initialAmount + input.monthlyTopUp * input.periodMonths
    val interestEarned = balance - totalDeposited

    // Pair — пара значений: (итоговая сумма, начисленные проценты)
    return Pair(balance, interestEarned)
}

// Определяет доступную ставку по сроку
fun getRateForPeriod(months: Int): Double {
    return when {
        months < 6  -> 15.0
        months < 12 -> 10.0
        else        -> 5.0
    }
}
