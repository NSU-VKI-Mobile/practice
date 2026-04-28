package ci.nsu.moble.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {

    // Сохранить расчёт
    suspend fun saveCalculation(deposit: DepositCalculation) {
        dao.insert(deposit)
    }


    // Получить все расчёты (как поток данных)
    fun getAllCalculations(): Flow<List<DepositCalculation>> {
        return dao.getAll()
    }

    // Получить конкретный расчёт по ID
    fun getCalculationById(id: Long): Flow<DepositCalculation?> {
        return dao.getById(id)
    }
}