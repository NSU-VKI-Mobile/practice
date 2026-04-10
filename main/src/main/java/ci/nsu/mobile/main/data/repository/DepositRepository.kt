package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepCalcs
import ci.nsu.mobile.main.data.database.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    // Получить все расчёты (поток данных)
    fun getAllCalculations(): Flow<List<DepCalcs>> {
        return dao.getAll()
    }

    // Получить конкретный расчёт по ID
    suspend fun getCalculationById(id: Long): DepCalcs? {
        return dao.getCalculationById(id)
    }

    // Добавить новый расчёт
    suspend fun insertCalculation(calculation: DepCalcs) {
        dao.insert(calculation)
    }

    // Удалить расчёт
    suspend fun deleteCalculation(calculation: DepCalcs) {
        dao.delete(calculation)
    }
}