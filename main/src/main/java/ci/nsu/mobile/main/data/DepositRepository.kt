package ci.nsu.mobile.main.data

import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    val history: Flow<List<DepositCalculation>> = dao.getAllHistory()

    suspend fun saveResult(calculation: DepositCalculation) {
        dao.insert(calculation)
    }
}