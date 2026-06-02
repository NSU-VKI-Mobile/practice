package ci.nsu.mobile.depositcalculator.data.repository

import ci.nsu.mobile.depositcalculator.data.database.DepositCalculationEntity
import ci.nsu.mobile.depositcalculator.data.database.DepositDao

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculationEntity>> {
        return depositDao.getAllCalculations()
    }

    suspend fun getCalculationById(id: Long): DepositCalculationEntity? {
        return depositDao.getCalculationById(id)
    }

    suspend fun saveCalculation(calculation: DepositCalculationEntity) {
        depositDao.insert(calculation)
    }

    suspend fun deleteCalculation(calculation: DepositCalculationEntity) {
        depositDao.delete(calculation)
    }

    suspend fun deleteAllCalculations() {
        depositDao.deleteAll()
    }
}
