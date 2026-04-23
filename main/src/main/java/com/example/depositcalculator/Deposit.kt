package com.example.depositcalculator

import java.time.temporal.TemporalAmount

data class Deposit(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double
)