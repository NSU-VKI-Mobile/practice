package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation

class DepositRepository(private val db: AppDatabase) {

    suspend fun getAllCalculations(): List<DepositCalculation> {
        return try {
            db.depositDao().getAllCalculations()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun insertCalculation(calculation: DepositCalculation) {
        db.depositDao().insert(calculation)
    }

    suspend fun clearHistory() {
        db.depositDao().deleteAll()
    }
}