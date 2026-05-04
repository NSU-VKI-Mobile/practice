package ci.nsu.mobile.main.util

import java.math.BigDecimal
import java.math.RoundingMode

data class CalculationResult(
    val finalAmount: Double,
    val interestEarned: Double
)

object DepositCalculator {

    /**
     * Выбирает процентную ставку по сроку вклада:
     * < 6 мес → 15.0%
     * 6..11 мес → 10.0%
     * >= 12 мес → 5.0%
     */
    fun selectRate(periodMonths: Int): Double = when {
        periodMonths < 6 -> 15.0
        periodMonths < 12 -> 10.0
        else -> 5.0
    }

    /**
     * Рассчитывает итоговую сумму и начисленные проценты.
     * Формула сложных процентов с ежемесячным пополнением:
     * monthlyRate = interestRate / 100 / 12
     * FinalAmount = initialAmount * (1 + monthlyRate)^periodMonths
     *             + monthlyTopUp * ((1 + monthlyRate)^periodMonths - 1) / monthlyRate
     *
     * Особый случай monthlyRate == 0.0:
     * FinalAmount = initialAmount + monthlyTopUp * periodMonths
     *
     * InterestEarned = FinalAmount - (initialAmount + monthlyTopUp * periodMonths)
     * Округление до 2 знаков через BigDecimal.
     */
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
