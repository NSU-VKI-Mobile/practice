package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.local.dao.DepositDao
import ci.nsu.mobile.calculations.data.local.entities.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    suspend fun saveCalculation(calculation: DepositCalculation)
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>
    suspend fun getCalculationById(id: Long, userId: Long): DepositCalculation?
    suspend fun deleteCalculation(calculation: DepositCalculation)
    suspend fun deleteAllForUser(userId: Long)
}

class DepositRepositoryImpl(
    private val depositDao: DepositDao
) : DepositRepository {

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        depositDao.insert(calculation)
    }

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId)
    }

    override suspend fun getCalculationById(id: Long, userId: Long): DepositCalculation? {
        return depositDao.getCalculationById(id, userId)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.delete(calculation)
    }

    override suspend fun deleteAllForUser(userId: Long) {
        depositDao.deleteAllForUser(userId)
    }
}