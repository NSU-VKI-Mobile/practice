package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.local.DepositEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DepositRepository @Inject constructor(
    private val dao: DepositDao
) {

    fun getAll() = dao.getAll()

    suspend fun insert(deposit: DepositEntity) {
        dao.insert(deposit)
    }
    suspend fun delete(entity: DepositEntity) {
        dao.delete(entity)
    }
    suspend fun deleteAll(){
        dao.deleteAll()
    }

    fun getDepositsByUserId(userId: Int) = dao.getDepositsByUserId(userId)


    suspend fun deleteDepositsByUserId(userId: Int) = dao.deleteDepositsByUserId(userId)

}