package ci.nsu.mobile.main.data.repository

import kotlinx.coroutines.flow.Flow
import ci.nsu.mobile.main.data.db.DepositDao
import ci.nsu.mobile.main.data.db.DepositCalculation
class DepositRepository(private val depositDao: DepositDao) {

    val allCalculations: Flow<List<DepositCalculation>> = depositDao.getAllCalculations()

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }
    suspend fun getById(id: Long): DepositCalculation? {
        return depositDao.getCalculationById(id)
    }
}