package ci.nsu.mobile.main

import kotlinx.coroutines.flow.Flow
//инкапсулирует логику работы с данными и промежуточный слой между ViewModel и БД
class CalculationRepository(
    private val calculationDao: CalculationDao
) {
    fun getAllCalculations(): Flow<List<Calculation>> {
        return calculationDao.getAllCalculations()
    }

    suspend fun insertCalculation(calculation: Calculation) {
        calculationDao.insertCalculation(calculation)
    }

    suspend fun deleteCalculation(calculation: Calculation) {
        calculationDao.deleteCalculation(calculation)
    }
}