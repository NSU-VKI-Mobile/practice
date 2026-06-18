package ci.nsu.mobile.main.domain.usecase

import ci.nsu.mobile.main.domain.model.DepositCalculation

class CalculateDepositUseCase {
    operator fun invoke(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double? = null
    ): DepositCalculation {
        val monthlyRate = interestRate / 100 / 12
        var finalAmount = initialAmount
        var totalTopUps = 0.0

        repeat(periodMonths) {
            finalAmount *= (1 + monthlyRate)
            monthlyTopUp?.let {
                finalAmount += it
                totalTopUps += it
            }
        }

        return DepositCalculation(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = finalAmount - initialAmount - totalTopUps
        )
    }

    fun getAvailableInterestRates(periodMonths: Int?): List<Double> {
        return when {
            periodMonths == null -> emptyList()
            periodMonths < 6 -> listOf(15.0)
            periodMonths < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
    }
}