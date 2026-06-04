package ci.nsu.mobile.data.repository

import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.data.db.DepositDao

class DepositRepository(private val dao: DepositDao) {

    suspend fun insertDeposit(item: DepositCalculation) {
        dao.insert(item)
    }

    suspend fun getHistory(login: String): List<DepositCalculation> {
        return dao.getHistory(login)
    }

    suspend fun deleteDeposit(item: DepositCalculation) {
        dao.delete(item)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}