package com.example.calculations

import com.example.calculations.data.repository.DepositRepository
import com.example.calculations.data.room.DepositCalculationEntity
import com.example.domain.interfaces.CalculationsProvider
import com.example.domain.models.DepositCalculation
import com.example.domain.models.DepositFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationsProviderImpl @Inject constructor(
    private val repository: DepositRepository
) : CalculationsProvider {

    override fun getCalculationsForUser(
        userId: Long,
        filter: DepositFilter?
    ): Flow<List<DepositCalculation>> {
        return repository.getFiltered(userId, filter).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        val entity = DepositCalculationEntity(
            userId = calculation.userId,
            initialAmount = calculation.initialAmount,
            periodMonths = calculation.periodMonths,
            interestRate = calculation.interestRate,
            monthlyTopUp = calculation.monthlyTopUp,
            finalAmount = calculation.finalAmount,
            interestEarned = calculation.interestEarned,
            calculationDate = calculation.calculationDate
        )
        repository.insertDeposit(entity)
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        repository.deleteDepositById(calculationId)
    }

    private fun DepositCalculationEntity.toDomain(): DepositCalculation {
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
}
