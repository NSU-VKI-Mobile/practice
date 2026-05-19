package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    val calculations: Flow<List<DepositCalculation>> = dao.getAllCalculations()

    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation)
    }
}
