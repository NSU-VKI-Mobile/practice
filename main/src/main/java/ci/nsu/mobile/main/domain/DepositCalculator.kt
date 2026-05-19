package ci.nsu.mobile.main.domain

import kotlin.math.round

data class DepositResult(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double
)

object DepositCalculator {
    fun availableRates(periodMonths: Int?): List<Double> {
        if (periodMonths == null || periodMonths <= 0) {
            return emptyList()
        }

        return listOf(
            when {
                periodMonths < 6 -> 15.0
                periodMonths < 12 -> 10.0
                else -> 5.0
            }
        )
    }

    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ): DepositResult {
        val monthlyRate = interestRate / 100.0 / 12.0
        val topUp = monthlyTopUp ?: 0.0
        var balance = initialAmount
        var totalTopUps = 0.0

        repeat(periodMonths) {
            balance += balance * monthlyRate
            balance += topUp
            totalTopUps += topUp
        }

        val finalAmount = roundMoney(balance)
        val interestEarned = roundMoney(finalAmount - initialAmount - totalTopUps)

        return DepositResult(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned
        )
    }

    private fun roundMoney(value: Double): Double {
        return round(value * 100.0) / 100.0
    }
}