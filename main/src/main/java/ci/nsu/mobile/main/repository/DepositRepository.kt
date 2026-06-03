package ci.nsu.mobile.main.repository


import android.content.Context
import ci.nsu.mobile.mai.database.DepositCalculation
import ci.nsu.mobile.mai.database.DepositDao
import ci.nsu.mobile.mai.database.DepositDatabase
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return depositDao.getAllCalculations()
    }

    suspend fun saveCalculation(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return depositDao.getCalculationById(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: DepositRepository? = null

        fun getInstance(context: Context): DepositRepository {
            return INSTANCE ?: synchronized(this) {
                val database = DepositDatabase.getDatabase(context)
                INSTANCE ?: DepositRepository(database.depositDao()).also { INSTANCE = it }
            }
        }
    }
}