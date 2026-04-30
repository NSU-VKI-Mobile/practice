package ci.nsu.mobile.main.data.local

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {
    val allCalculations: Flow<List<DepositCalculation>> = depositDao.getAllCalculations()

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insert(calculation)
    }
}