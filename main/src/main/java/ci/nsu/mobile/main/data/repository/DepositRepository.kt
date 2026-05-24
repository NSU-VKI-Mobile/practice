package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.room.DepositCalculationEntity
import ci.nsu.mobile.main.data.room.DepositDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculationEntity) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun getFiltered(userId: Long, filter: DepositFilter?): Flow<List<DepositCalculationEntity>> {
        return depositDao.GetAll(userId).map { deposits ->
            if (filter == null) deposits
            else {
                var filtered = deposits
                filter.amountStart?.let { min ->
                    filtered = filtered.filter { it.finalAmount >= min }
                }
                filter.amountFinish?.let { max ->
                    filtered = filtered.filter { it.finalAmount <= max }
                }
                filter.date?.let { date ->
                    filtered = filtered.filter { it.calculationDate >= date }
                }
                filter.rate?.let { rate ->
                    filtered = filtered.filter { it.interestRate == rate }
                }
                filtered
            }
        }
    }

    suspend fun deleteDeposit(deposit: DepositCalculationEntity) {
        depositDao.deleteDeposit(deposit)
    }
}
