package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    val allCalculations: Flow<List<DepositCalculation>> = dao.getAllCalculations()

    suspend fun insert(calculation: DepositCalculation): Long {
        return dao.insert(calculation)
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return dao.getCalculationById(id)
    }

    suspend fun delete(calculation: DepositCalculation) {
        dao.delete(calculation)
    }
}