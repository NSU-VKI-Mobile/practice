package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.local.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(
    private val depositDao: DepositDao
) {
    fun getCalculationsForUser(
        userId: Long,
        minAmount: Double?,
        maxAmount: Double?,
        fromDate: Long?,
        toDate: Long?
    ): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(
            userId = userId,
            minAmount = minAmount,
            maxAmount = maxAmount,
            fromDate = fromDate,
            toDate = toDate
        )
    }

    fun getCalculationByIdForUser(id: Long, userId: Long): Flow<DepositCalculation?> {
        return depositDao.getCalculationByIdForUser(id, userId)
    }

    suspend fun insertCalculation(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }

    suspend fun deleteCalculationByIdForUser(id: Long, userId: Long) {
        depositDao.deleteCalculationByIdForUser(id, userId)
    }
}
