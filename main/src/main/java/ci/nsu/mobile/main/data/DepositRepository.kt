package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(
    private val depositDao: DepositDao
) {

    // Получить всю историю
    val allCalculations: Flow<List<DepositCalculation>> =
        depositDao.getAllCalculations()

    // Сохранить расчёт
    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insert(calculation)
    }

    // Очистить историю
    suspend fun deleteAll() {
        depositDao.deleteAll()
    }
}