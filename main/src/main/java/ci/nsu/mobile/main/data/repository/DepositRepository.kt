package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insertCalculation(calculation)
    }

    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return dao.getCalculationsForUser(userId)
    }

    suspend fun deleteCalculation(calculation: DepositCalculation) {
        dao.deleteCalculation(calculation)
    }
}