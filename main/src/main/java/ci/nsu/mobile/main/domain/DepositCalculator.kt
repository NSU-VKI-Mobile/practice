package ci.nsu.mobile.main.domain

import ci.nsu.mobile.main.data.local.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class DepositCalculator {

    fun calculate(
        initialAmount: Double,
        months: Int,
        monthlyTopUp: Double = 0.0
    ): DepositCalculation {

        val rate = when {
            months < 6 -> 15.0
            months < 12 -> 10.0
            else -> 5.0
        }

        val monthlyRate = rate / 100 / 12
        var currentAmount = initialAmount

        for (i in 1..months) {
            currentAmount = (currentAmount + monthlyTopUp) * (1 + monthlyRate)
        }

        val totalInvested = initialAmount + (monthlyTopUp * months)
        val interestEarned = currentAmount - totalInvested

        return DepositCalculation(
            initialAmount = initialAmount,
            months = months,
            rate = rate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = (currentAmount * 100).roundToInt() / 100.0,
            profit = (interestEarned * 100).roundToInt() / 100.0,
            date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
        )
    }
}