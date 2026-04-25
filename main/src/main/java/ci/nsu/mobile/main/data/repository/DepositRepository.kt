package ci.nsu.mobile.main.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.local.DepositEntity
import kotlinx.coroutines.flow.StateFlow


@Singleton
class DepositRepository @Inject constructor(
    private val dao: DepositDao
) {

    suspend fun insert(calculation: DepositEntity) {
        dao.insert(calculation)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun getAll(): Flow<List<DepositEntity>> {
        return dao.getAll()
    }
}