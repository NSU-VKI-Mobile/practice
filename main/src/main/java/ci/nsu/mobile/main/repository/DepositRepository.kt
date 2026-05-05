package ci.nsu.mobile.main.repository

import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {
    val allCalculations: Flow<List<DepositCalculation>> = depositDao.getAllCalculations()

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }
}

//UI-слой - Слой репозитория - ??? - Слой БД