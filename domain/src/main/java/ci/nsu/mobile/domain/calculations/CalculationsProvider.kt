package ci.nsu.mobile.domain.calculations

import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(calculationId: Long)

    fun getCalculationsForCurrentUser(): Flow<List<DepositCalculation>>
    suspend fun saveCalculationForCurrentUser(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double,
        finalAmount: Double,
        interestEarned: Double
    )
}