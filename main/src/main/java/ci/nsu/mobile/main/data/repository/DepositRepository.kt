package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositCalculationEntity
import ci.nsu.mobile.main.data.database.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculationEntity) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun GetAll(): Flow<List<DepositCalculationEntity>> {
        return depositDao.GetAll()
    }

    fun GetById(id: Long): Flow<DepositCalculationEntity> {
        return depositDao.GetById(id)
    }
}
