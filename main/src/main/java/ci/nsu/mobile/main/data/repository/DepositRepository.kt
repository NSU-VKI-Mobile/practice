package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(calculation: DepositCalculation)
    fun getCalculations(userId: Long): Flow<List<DepositCalculation>>
}