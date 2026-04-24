package ci.nsu.moble.main.data.repository

import ci.nsu.moble.main.data.database.DepositCalculationEntity
import ci.nsu.moble.main.data.database.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    fun getAllCalculations(): Flow<List<DepositCalculationEntity>> = dao.getAllCalculations()

    suspend fun getCalculationById(id: Long): DepositCalculationEntity? = dao.getCalculationById(id)

    suspend fun saveCalculation(calculation: DepositCalculationEntity) {
        dao.insertCalculation(calculation)
    }
}