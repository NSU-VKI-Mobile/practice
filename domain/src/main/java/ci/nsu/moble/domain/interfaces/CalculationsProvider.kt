package ci.nsu.moble.domain.interfaces

import ci.nsu.moble.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface CalculationsProvider
{
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(calculationId: Long)
}