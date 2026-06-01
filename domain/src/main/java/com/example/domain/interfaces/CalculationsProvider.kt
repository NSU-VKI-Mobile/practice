package com.example.domain.interfaces

import com.example.domain.models.DepositCalculation
import com.example.domain.models.DepositFilter
import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long, filter: DepositFilter?): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(calculationId: Long)
}