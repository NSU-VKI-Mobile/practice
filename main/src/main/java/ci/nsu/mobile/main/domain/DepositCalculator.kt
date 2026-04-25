package ci.nsu.mobile.main.domain

import ci.nsu.mobile.main.data.local.DepositEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DepositCalculator {
    fun calculate(initialAmount: Double, months: Int, rate: Double, monthlyTopUp: Double): DepositEntity {

        val n = 12
        val t = months / 12.0

        val monthlyRate = rate / n

        // Основной вклад
        val base = initialAmount * Math.pow(1 + monthlyRate, n * t)

        // Пополнения
        val topUp = if (monthlyTopUp > 0) {
            monthlyTopUp * (
                    (Math.pow(1 + monthlyRate, n * t) - 1) / monthlyRate
                    )
        } else 0.0

        val total = base + topUp
        val totalInvested = initialAmount + monthlyTopUp * months
        val profit = total - totalInvested

        return DepositEntity(
            initialAmount = initialAmount,
            months = months,
            rate = rate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = total,
            profit = profit,
            date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
        )
    }
}