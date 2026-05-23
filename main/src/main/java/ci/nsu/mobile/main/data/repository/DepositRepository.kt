package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.room.DepositCalculationEntity
import ci.nsu.mobile.main.data.room.DepositDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculationEntity) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun getAll(userId: Long): Flow<List<DepositCalculationEntity>> {
        return depositDao.GetAll(userId)
    }

    suspend fun findDuplication(entity: DepositCalculationEntity) : DepositCalculationEntity? {
        return depositDao.findDuplication(entity.initialAmount, entity.periodMonths, entity.interestRate, entity.monthlyTopUp)
    }

    suspend fun deleteDeposit(deposit: DepositCalculationEntity) {
        depositDao.deleteDeposit(deposit)
    }
}
