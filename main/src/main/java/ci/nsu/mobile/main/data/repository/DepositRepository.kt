package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositEntity
import ci.nsu.mobile.main.data.local.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    // Получаем всю историю
    val allDeposits: Flow<List<DepositEntity>> = depositDao.getAllDeposits()

    // Сохраняем новый расчет
    suspend fun saveCalculation(deposit: DepositEntity) {
        depositDao.insert(deposit)
    }
}