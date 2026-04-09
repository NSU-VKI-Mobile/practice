package ci.nsu.mobile.main.data.repository
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val db: AppDatabase) {
    fun getAllCalculations(): Flow<List<DepositCalculation>> =
        db.depositDao().getAllCalculations()
    suspend fun insertCalculation(calculation: DepositCalculation) =
        db.depositDao().insert(calculation)
    suspend fun clearHistory() =
        db.depositDao().deleteAll()
}