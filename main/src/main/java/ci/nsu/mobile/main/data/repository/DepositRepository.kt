package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.data.database.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculation) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun GetAll(): Flow<List<DepositCalculation>> {
        return depositDao.GetAll()
    }

    fun GetById(id: Long): Flow<DepositCalculation> {
        return depositDao.GetById(id)
    }
    companion object {
        @Volatile
        private var INSTANCE: DepositRepository? = null

        fun getInstance(dao: DepositDao): DepositRepository {
            return INSTANCE ?: synchronized(this) {
                DepositRepository(dao).also { INSTANCE = it }
            }
        }
    }
}
