package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositEntity
import ci.nsu.mobile.main.data.local.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    // Теперь получаем историю не всю подряд, а только для конкретного логина
    fun getDepositsForUser(login: String): Flow<List<DepositEntity>> {
        return depositDao.getDepositsForUser(login)
    }

    // Сохраняем новый расчет
    suspend fun saveCalculation(deposit: DepositEntity) {
        depositDao.insert(deposit)
    }

    suspend fun delete(deposit: DepositEntity) {
        depositDao.delete(deposit)
    }
}