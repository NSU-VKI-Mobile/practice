package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.database.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {
    suspend fun insertCalculation(calculation: DepositCalculation) {
        depositDao.insert(calculation)
    }

    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return depositDao.getAllCalculations()
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return depositDao.getCalculationById(id)
    }
}