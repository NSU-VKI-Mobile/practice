package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.roomDatabase.DepositDao
import ci.nsu.mobile.main.data.roomDatabase.DepositEntity

class DepositRepository(private val dao: DepositDao) {

    fun getAll() = dao.getAll()

    suspend fun insert(deposit: DepositEntity) {
        dao.insert(deposit)
    }
}