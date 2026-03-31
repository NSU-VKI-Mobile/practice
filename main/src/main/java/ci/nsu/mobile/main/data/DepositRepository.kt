package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    // Получаем всю историю
    val allDeposits: Flow<List<DepositEntity>> = depositDao.getAllDeposits()

    // Сохраняем новый расчет
    suspend fun saveCalculation(deposit: DepositEntity) {
        depositDao.insert(deposit)
    }
}