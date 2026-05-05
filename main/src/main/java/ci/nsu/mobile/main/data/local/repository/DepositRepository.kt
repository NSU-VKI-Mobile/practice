package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.local.DepositEntity
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    fun getAll(): Flow<List<DepositEntity>> = dao.getAll()

    suspend fun insert(entity: DepositEntity) {
        dao.insert(entity)
    }
}