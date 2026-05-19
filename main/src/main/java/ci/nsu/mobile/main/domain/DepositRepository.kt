package ci.nsu.mobile.main.domain

import ci.nsu.mobile.main.data.DepositCalculationEntity
import ci.nsu.mobile.main.data.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculationEntity) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun getAll(): Flow<List<DepositCalculationEntity>> {
        return depositDao.GetAll()
    }

    suspend fun findDuplication(entity: DepositCalculationEntity) : DepositCalculationEntity? {
        return depositDao.findDuplication(entity.initialAmount, entity.periodMonths, entity.interestRate, entity.monthlyTopUp)
    }
}