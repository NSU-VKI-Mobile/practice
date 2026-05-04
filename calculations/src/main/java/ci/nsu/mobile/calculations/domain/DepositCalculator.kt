package ci.nsu.mobile.calculations.domain

import ci.nsu.mobile.domain.model.CalculationResult

object DepositCalculator {
    fun availableRates(periodMonths: Int): List<String> {
        return when {
            periodMonths <= 0 -> emptyList()
            periodMonths < 6 -> listOf("15")
            periodMonths in 6..11 -> listOf("10")
            else -> listOf("5")
        }
    }

    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double
    ): CalculationResult {
        var currentSum = initialAmount
        var totalInterest = 0.0
        val monthlyRate = interestRate / 100 / 12

        repeat(periodMonths) {
            currentSum += monthlyTopUp
            val interestForMonth = currentSum * monthlyRate
            totalInterest += interestForMonth
            currentSum += interestForMonth
        }

        return CalculationResult(
            finalAmount = currentSum,
            interestEarned = totalInterest
        )
    }
}
