package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insertCalculation(calculation)
    }

//    fun getAllCalculations(): Flow<List<DepositCalculation>> = dao.getCalculationsForUser()
//
//    suspend fun getCalculationById(id: Long): DepositCalculation? = dao.getCalculationById(id)
//
    suspend fun deleteCalculation(calculation: DepositCalculation) {
        dao.deleteCalculation(calculation)
    }
}