package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> =
        depositDao.getAllCalculations()

    suspend fun insertCalculation(calculation: DepositCalculation) =
        depositDao.insertCalculation(calculation)

    suspend fun getLastTenCalculations(): List<DepositCalculation> =
        depositDao.getLastTenCalculations()

    suspend fun deleteCalculation(id: Long) =
        depositDao.deleteCalculation(id)

    suspend fun deleteAllCalculations() =
        depositDao.deleteAllCalculations()
}