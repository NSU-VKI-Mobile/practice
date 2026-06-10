package ci.nsu.mobile.domain

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.flow.Flow

interface CalculationsNavigator {
    fun navigateToNewCalculation(context: Context, userId: Long)
    fun navigateToMyCalculations(context: Context, userId: Long)
    fun openCalculationFlow(activity: Activity, userId: Long)
}

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(calculationId: Long)
}

data class DepositCalculation(
    val id: Long = 0,
    val userId: Long,
    val amount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val result: Double,
    val date: Long = System.currentTimeMillis()
)