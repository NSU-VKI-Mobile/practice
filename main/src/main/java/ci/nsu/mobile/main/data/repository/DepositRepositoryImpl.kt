package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepositoryImpl(private val dao: DepositDao) : DepositRepository {
    override suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        dao.delete(calculation)
    }

    override fun getCalculations(userId: Long): Flow<List<DepositCalculation>> {
        return dao.getCalculationsByUser(userId)
    }
}