package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.database.AppDatabase
import ci.nsu.mobile.main.database.DepositDao
import ci.nsu.mobile.main.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository private constructor(private val depositDao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return depositDao.getAllCalculations()
    }

    suspend fun saveCalculation(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return depositDao.getCalculationById(id)
    }

    suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.deleteCalculation(calculation)
    }

    suspend fun deleteAllCalculations() {
        depositDao.deleteAllCalculations()
    }

    companion object {
        @Volatile
        private var INSTANCE: DepositRepository? = null

        fun getInstance(database: AppDatabase): DepositRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DepositRepository(database.depositDao())
                INSTANCE = instance
                instance
            }
        }
    }
}