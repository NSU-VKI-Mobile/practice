package ci.nsu.mobile.domain.calculations

import ci.nsu.mobile.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    fun saveCalculation(calculation: DepositCalculation)
    fun deleteCalculation(calculationId: Long)
}