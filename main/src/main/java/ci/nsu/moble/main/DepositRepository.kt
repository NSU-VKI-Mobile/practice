package ci.nsu.moble.main

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    fun getAllCalculations(): Flow<List<DepositCalculation>> = dao.getAllCalculations()

    suspend fun getCalculationById(id: Long): DepositCalculation? = dao.getCalculationById(id)

    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insertCalculation(calculation)
    }
}