package ci.nsu.mobile.main.domain.repository

import ci.nsu.mobile.main.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    suspend fun saveCalculation(userId: Long, calculation: DepositCalculation): Long
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun getCalculationById(id: Long): DepositCalculation?
    suspend fun deleteCalculation(id: Long)
}