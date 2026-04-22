package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.db.DepositDao
import ci.nsu.mobile.main.data.db.DepositEntity

class DepositRepository(private val depositDao: DepositDao) {

    suspend fun insertDeposit(deposit: DepositEntity) {
        depositDao.insertDeposit(deposit)
    }

    suspend fun getHistory(): List<DepositEntity> {
        return depositDao.getAllDeposits()
    }

    suspend fun getDepositById(id: Long): DepositEntity? {
        return depositDao.getDepositById(id)
    }
}