package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    val calculations: Flow<List<DepositCalculation>> = dao.getAllCalculations()

    fun getCalculationById(id: Long): Flow<DepositCalculation?> {
        return dao.getCalculationById(id)
    }

    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    suspend fun deleteCalculationById(id: Long) {
        dao.deleteCalculationById(id)
    }

    suspend fun clearAllCalculations() {
        dao.clearAllCalculations()
    }
}
