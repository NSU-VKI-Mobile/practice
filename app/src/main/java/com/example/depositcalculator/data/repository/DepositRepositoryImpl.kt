package com.example.depositcalculator.data.repository

import com.example.depositcalculator.data.local.DepositCalculationEntity
import com.example.depositcalculator.data.local.DepositDao
import com.example.depositcalculator.domain.repository.DepositRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DepositRepositoryImpl @Inject constructor(
    private val dao: DepositDao
) : DepositRepository {
    override suspend fun saveCalculation(calculation: DepositCalculationEntity) {
        dao.insert(calculation)
    }

    override fun getAllCalculations(): Flow<List<DepositCalculationEntity>> {
        return dao.getAllCalculations()
    }

    override fun getCalculationById(id: Long): Flow<DepositCalculationEntity?> {
        return dao.getCalculationById(id)
    }
}