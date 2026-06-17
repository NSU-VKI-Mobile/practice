package ci.nsu.mobile.domain.calculations

import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface CalculationsProvider {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun saveCalculation(calculation: DepositCalculation)
    suspend fun deleteCalculation(id: Long)

    // Чистая функция расчета (бизнес-логика)
    fun calculateDeposit(
        amount: Double,
        months: Int,
        rate: Double,
        topUp: Double,
        userId: Long // Добавляем userId, чтобы сразу создать объект с ID
    ): DepositCalculation
}