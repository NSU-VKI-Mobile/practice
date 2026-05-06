package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.data.*

class DepositRepository(private val dao: DepositDao) {

    val allData = dao.getAll()

    suspend fun insert(item: DepositCalculation) {
        dao.insert(item)
    }
}