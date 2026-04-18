package ci.nsu.mobile.main.Data.Repository

import ci.nsu.mobile.main.Data.DAO.CalculationDao
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import kotlinx.coroutines.flow.Flow

class CalculationRepository(private val dao: CalculationDao) {
    fun getAllCalculations(): Flow<List<CalculationEntity>> = dao.getAllCalculations()

    suspend fun insertCalculation(calculation: CalculationEntity) = dao.insert(calculation)

    suspend fun deleteCalculation(id: Long) = dao.deleteById(id)
    fun getCalculationById(id: Long): Flow<CalculationEntity?> = dao.getCalculationById(id)
}