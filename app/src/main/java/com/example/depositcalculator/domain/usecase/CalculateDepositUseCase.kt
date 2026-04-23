package com.example.depositcalculator.domain.usecase

import javax.inject.Inject
import kotlin.math.pow

class CalculateDepositUseCase @Inject constructor() {

    operator fun invoke(
        initial: Double,
        months: Int,
        annualRate: Double,
        monthlyTopUp: Double
    ): Pair<Double, Double> {
        if (months <= 0) return Pair(initial, 0.0)
        val monthlyRate = annualRate / 12 / 100
        val finalAmount = if (monthlyRate == 0.0) {
            initial + monthlyTopUp * months
        } else {
            initial * (1.0 + monthlyRate).pow(months) +
                    monthlyTopUp * ((1.0 + monthlyRate).pow(months) - 1) / monthlyRate
        }
        val totalAdded = monthlyTopUp * months
        val interest = finalAmount - initial - totalAdded
        return Pair(finalAmount, interest)
    }

    fun getAvailableRate(months: Int): Double = when {
        months < 6 -> 15.0
        months < 12 -> 10.0
        else -> 5.0
    }
}