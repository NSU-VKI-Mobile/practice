package ci.nsu.mobile.main.util

import java.math.BigDecimal
import java.math.RoundingMode

data class CalculationResult(
    val finalAmount: Double,
    val interestEarned: Double
)

object DepositCalculator {


    fun selectRate(periodMonths: Int): Double = when {
        periodMonths < 6 -> 15.0
        periodMonths < 12 -> 10.0
        else -> 5.0
    }

    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double
    ): CalculationResult {
        val monthlyRate = interestRate / 100.0 / 12.0
        val finalAmount: Double
        if (monthlyRate == 0.0) {
            finalAmount = initialAmount + monthlyTopUp * periodMonths
        } else {
            val factor = Math.pow(1.0 + monthlyRate, periodMonths.toDouble())
            finalAmount = initialAmount * factor + monthlyTopUp * (factor - 1.0) / monthlyRate
        }
        val totalContributions = initialAmount + monthlyTopUp * periodMonths
        val interestEarned = finalAmount - totalContributions

        val roundedFinal = BigDecimal(finalAmount).setScale(2, RoundingMode.HALF_UP).toDouble()
        val roundedInterest = BigDecimal(interestEarned).setScale(2, RoundingMode.HALF_UP).toDouble()

        return CalculationResult(
            finalAmount = roundedFinal,
            interestEarned = roundedInterest
        )
    }
}
