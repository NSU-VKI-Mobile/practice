package ci.nsu.mobile.calculations.data.mapper

import ci.nsu.mobile.calculations.data.local.entity.DepositCalculationEntity
import ci.nsu.mobile.domain.models.DepositCalculation

fun DepositCalculationEntity.toDomain() = DepositCalculation(
    id, userId, initialAmount, periodMonths, interestRate, monthlyTopUp, finalAmount, interestEarned, calculationDate
)

fun DepositCalculation.toEntity() = DepositCalculationEntity(
    id, userId, initialAmount, periodMonths, interestRate, monthlyTopUp, finalAmount, interestEarned, calculationDate
)