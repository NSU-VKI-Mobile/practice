package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.room.DepositCalculation
import ci.nsu.mobile.main.data.room.DepositDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculation) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun getFiltered(userId: Long, filter: DepositFilter?): Flow<List<DepositCalculation>> {
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

    suspend fun deleteDeposit(deposit: DepositCalculation) {
        depositDao.deleteDeposit(deposit)
    }
}
