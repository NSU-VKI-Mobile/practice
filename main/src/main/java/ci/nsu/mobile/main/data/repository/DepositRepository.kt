package ci.nsu.mobile.main.data.repository


import ci.nsu.mobile.main.data.database.CalculationDao
import ci.nsu.mobile.main.data.database.CalculationEntity
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: CalculationDao) {
    fun getAllCalculations(): Flow<List<CalculationEntity>> = dao.getAllCalculations()

    suspend fun insertCalculation(calculation: CalculationEntity) = dao.insert(calculation)

    suspend fun deleteCalculation(id: Long) = dao.deleteById(id)
    fun getCalculationById(id: Long): Flow<CalculationEntity?> = dao.getCalculationById(id)
}