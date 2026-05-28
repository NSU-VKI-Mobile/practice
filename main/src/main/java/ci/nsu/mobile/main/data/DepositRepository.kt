package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    fun getAllCalculations(): Flow<List<DepositCalculation>> = dao.getAllCalculations()

    suspend fun getCalculationById(id: Long): DepositCalculation? = dao.getCalculationById(id)

    suspend fun deleteCalculation(calculation: DepositCalculation) {
        dao.delete(calculation)
    }
}