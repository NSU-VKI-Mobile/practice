package ci.nsu.mobile.domain.calculations

import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(calculationId: Long)
}