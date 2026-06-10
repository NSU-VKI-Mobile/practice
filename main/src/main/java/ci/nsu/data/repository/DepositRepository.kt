package ci.nsu.data.repository

import ci.nsu.data.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    // Получить стрим данных с расчетами конкретного пользователя
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>

    // Сохранить новый расчет в базу данных
    suspend fun insertCalculation(calculation: DepositCalculation)

    // Удалить расчет из базы данных
    suspend fun deleteCalculation(calculation: DepositCalculation)
}
