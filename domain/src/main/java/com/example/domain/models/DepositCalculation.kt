package com.example.domain.models

data class DepositCalculation(
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Int,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
)