package ci.nsu.moble.main.data

import kotlinx.coroutines.flow.Flow

class DepositRepository(
    private val depositDao: DepositDao
) {
    fun getAllCalculations(): Flow<List<DepositCalculationEntity>> = depositDao.getAll()

    suspend fun getCalculationById(id: Long): DepositCalculationEntity = depositDao.getById(id)

    suspend fun saveCalculation(item: DepositCalculationEntity) {
        depositDao.insert(item)
    }
}