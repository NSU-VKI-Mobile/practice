package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {
    val allDeposits: Flow<List<DepositEntity>> = depositDao.getAllDeposits()
    suspend fun insert(deposit: DepositEntity) = depositDao.insert(deposit)
}