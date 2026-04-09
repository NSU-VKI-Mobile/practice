package com.example.depositcalculator

import com.example.depositcalculator.Deposit
import java.time.Month

object DepositCalculator{

    fun calculate(
        initialAmount: Double,
        months: Int,
        rate: Double,
        monthlyTopUp: Double?
    ): Deposit{

        var total = initialAmount
        val monthlyRate = rate / 100 / 12

        repeat(months){
            total += total * monthlyRate

            if (monthlyTopUp != null) {
                total += monthlyTopUp
            }
        }

        val totalTopUp = (monthlyTopUp ?: 0.0) * months
        val interest = total - initialAmount - totalTopUp

        return Deposit(
            initialAmount = initialAmount,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = total,
            interestEarned = interest
        )
    }
}