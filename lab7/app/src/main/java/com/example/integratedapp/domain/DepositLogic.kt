package com.example.integratedapp.domain

// Бизнес-логика расчёта вклада (как в лабе 5)

data class DepositInput(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double = 0.0
)

// Считает итоговую сумму и начисленные проценты
// Каждый месяц: добавляем пополнение → начисляем проценты
fun calculateDeposit(input: DepositInput): Pair<Double, Double> {
    var balance = input.initialAmount
    val monthlyRate = input.interestRate / 100.0 / 12.0

    repeat(input.periodMonths) {
        balance += input.monthlyTopUp
        balance += balance * monthlyRate
    }

    val totalDeposited = input.initialAmount + input.monthlyTopUp * input.periodMonths
    val interestEarned = balance - totalDeposited
    return Pair(balance, interestEarned)
}

// Ставка по сроку
fun getRateForPeriod(months: Int): Double = when {
    months < 6  -> 15.0
    months < 12 -> 10.0
    else        -> 5.0
}
