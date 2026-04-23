package com.example.depositcalculator.domain.repository

import com.example.depositcalculator.data.local.DepositCalculationEntity
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    suspend fun saveCalculation(calculation: DepositCalculationEntity)
    fun getAllCalculations(): Flow<List<DepositCalculationEntity>>
    fun getCalculationById(id: Long): Flow<DepositCalculationEntity?>
}