package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.database.DepositDao

class DepositRepository(private val dao: DepositDao) {

    val all = dao.getAll()

    suspend fun insert(item: DepositCalculation) {
        dao.insert(item)
    }
}

