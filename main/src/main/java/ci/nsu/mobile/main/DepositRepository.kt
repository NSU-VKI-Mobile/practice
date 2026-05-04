package ci.nsu.mobile.main

import android.content.Context
import kotlinx.coroutines.flow.Flow

class DepositRepository(context: Context) {

    private val dao = AppDatabase.getDatabase(context).depositDao()

    fun getAllCalculations(): Flow<List<DepositEntity>> {
        return dao.getAllCalculations()
    }

    suspend fun saveCalculation(calculation: DepositEntity) {
        dao.insert(calculation)
        println("Сохранено в БД: ${calculation.finalAmount}")
    }

    suspend fun clearHistory() {
        dao.deleteAll()
        println("История очищена")
    }
}

