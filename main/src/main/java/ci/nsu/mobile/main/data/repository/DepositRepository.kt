package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositDao
import ci.nsu.mobile.main.data.database.DepositEntity
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositEntity>> = dao.getAllCalculations()

    suspend fun saveCalculation(calculation: DepositEntity) {
        dao.insert(calculation)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun delete(calculation: DepositEntity) {
        dao.delete(calculation)
    }
}