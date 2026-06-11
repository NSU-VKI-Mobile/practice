package ci.nsu.mobile.domain

import kotlinx.coroutines.flow.Flow

data class DepositCalculationDomain(
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculationDomain>>
    suspend fun saveCalculation(calculation: DepositCalculationDomain)
    suspend fun deleteCalculation(calculationId: Long)
}