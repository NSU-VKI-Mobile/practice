package ci.nsu.mobile.domain.calculations

import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

/**
 * Public calculations API exposed by the calculations module.
 */
interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    fun saveCalculation(calculation: DepositCalculation)
    fun deleteCalculation(calculationId: Long)
}
