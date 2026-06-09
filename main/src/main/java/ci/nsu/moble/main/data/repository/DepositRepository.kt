package ci.nsu.moble.main.data.repository

import ci.nsu.moble.main.data.database.DepositDao
import ci.nsu.moble.main.data.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(
    private val depositDao: DepositDao
) {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId)
    }

    suspend fun saveCalculation(calculation: DepositCalculation) {
        depositDao.insert(calculation)
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return depositDao.getCalculationById(id)
    }

    suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.delete(calculation)
    }
}