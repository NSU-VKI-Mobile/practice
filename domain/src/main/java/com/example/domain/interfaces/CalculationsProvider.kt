package com.example.domain.interfaces

import com.example.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    fun saveCalculation(calculation: DepositCalculation)
    fun deleteCalculation(calculationId: Long)
}