package ci.nsu.data.repository

import ci.nsu.data.local.DepositDao
import ci.nsu.data.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepositoryImpl(
    private val depositDao: DepositDao
) : DepositRepository {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId)
    }

    override suspend fun insertCalculation(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.deleteCalculation(calculation)
    }
}
