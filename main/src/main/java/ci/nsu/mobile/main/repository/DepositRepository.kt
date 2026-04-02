package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val db: AppDatabase) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return db.depositDao().getAllCalculations()
    }

    suspend fun saveCalculation(calculation: DepositCalculation) {
        db.depositDao().insert(calculation)
    }

    suspend fun clearHistory() {
        db.depositDao().deleteAll()
    }
}