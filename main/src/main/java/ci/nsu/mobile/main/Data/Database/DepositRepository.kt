package ci.nsu.mobile.main.Data.Database

import androidx.lifecycle.LiveData

class DepositRepository(private val dao: DepositDao) {
    // Для конкретного пользователя
    fun getAllCalculationsForUser(userId: Long): LiveData<List<DepositCalculation>> =
        dao.getAllDepositsForUser(userId)

    // Для обратной совместимости
    val allCalculations: LiveData<List<DepositCalculation>> = dao.getAllDeposits()

    suspend fun insert(calculation: DepositCalculation) = dao.insert(calculation)
}