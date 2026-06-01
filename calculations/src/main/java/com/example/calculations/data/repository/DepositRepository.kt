package com.example.calculations.data.repository


import com.example.calculations.data.room.DepositCalculationEntity
import com.example.calculations.data.room.DepositDao
import com.example.domain.models.DepositFilter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(val depositDao: DepositDao) {
    suspend fun insertDeposit(depositCalculation: DepositCalculationEntity) {
        depositDao.InsertDeposit(depositCalculation)
    }

    fun getFiltered(userId: Long, filter: DepositFilter?): Flow<List<DepositCalculationEntity>> {
        return depositDao.GetFiltered(
            userId = userId,
            amountStart = filter?.amountStart,
            amountFinish = filter?.amountFinish,
            date = filter?.date,
            rate = filter?.rate
        )
    }

    suspend fun deleteDeposit(deposit: DepositCalculationEntity) {
        depositDao.deleteDeposit(deposit)
    }
    suspend fun deleteDepositById(depositId: Long) {
        depositDao.deleteDepositById(depositId)
    }
}
