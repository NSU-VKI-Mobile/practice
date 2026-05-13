package ci.nsu.mobile.calculations.data.mapper

import ci.nsu.mobile.calculations.data.entity.DepositEntity
import ci.nsu.mobile.domain.model.DepositCalculation

fun DepositEntity.toDomain() = DepositCalculation(
    id = id,
    userId = userId,
    startAmount = startAmount,
    months = months,
    rate = rate,
    monthlyTopUp = monthlyTopUp,
    finalAmount = finalAmount,
    profit = profit,
    date = date
)

fun DepositCalculation.toEntity() = DepositEntity(
    id = id,
    userId = userId,
    startAmount = startAmount,
    months = months,
    rate = rate,
    monthlyTopUp = monthlyTopUp,
    finalAmount = finalAmount,
    profit = profit,
    date = date
)