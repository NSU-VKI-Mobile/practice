package com.example.calculations.provider

import com.example.calculations.data.repository.DepositRepository
import com.example.calculations.util.toDomain
import com.example.calculations.util.toEntity
import com.example.domain.interfaces.CalculationsProvider
import com.example.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl( private val repository: DepositRepository) : CalculationsProvider {
    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return repository.getDepositUser(userId).map { deposit -> deposit.map { it.toDomain() } }
    }

    override fun saveCalculation(calculation: DepositCalculation){
        repository.addDeposit(calculation.toEntity())
    }

    override fun deleteCalculation(calculationId: Long){
        repository.deleteDeposit(calculationId)
    }
}