package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.local.DepositEntity
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    fun getAll() = dao.getAll()

    suspend fun insert(entity: DepositEntity) {
        dao.insert(entity)
    }


    suspend fun delete(entity: DepositEntity) {
        dao.delete(entity)
    }
    suspend fun deleteAll() {
        dao.deleteAll()
    }
}