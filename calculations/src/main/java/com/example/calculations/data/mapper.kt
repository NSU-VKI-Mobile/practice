package com.example.calculations.data

import com.example.calculations.data.dbo.Deposit
import com.example.domain.model.DepositCalculation

fun DepositCalculation.toEntity(): Deposit {
    return Deposit(
        id = this.id,
        userId = this.userId,
        initialAmount = this.initialAmount,
        periodMonths = this.periodMonths,
        interestRate = this.interestRate,
        monthlyTopUp = this.monthlyTopUp,
        finalAmount = this.finalAmount,
        interestEarned = this.interestEarned,
        calculationDate = this.calculationDate
    )
}

fun Deposit.toDomain(): DepositCalculation {
    return DepositCalculation(
        id = this.id,
        userId = this.userId,
        initialAmount = this.initialAmount,
        periodMonths = this.periodMonths,
        interestRate = this.interestRate,
        monthlyTopUp = this.monthlyTopUp,
        finalAmount = this.finalAmount,
        interestEarned = this.interestEarned,
        calculationDate = this.calculationDate
    )
}