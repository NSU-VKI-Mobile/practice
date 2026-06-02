package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.dao.DepositDao
import ci.nsu.mobile.main.data.local.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    fun getCalculationsByUser(userId: Long): Flow<List<DepositCalculation>> {
        return dao.getByUserId(userId)
    }

    suspend fun insert(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}
