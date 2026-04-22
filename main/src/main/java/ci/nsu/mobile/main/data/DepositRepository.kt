package ci.nsu.mobile.main.data

import kotlinx.coroutines.flow.Flow

object DepositRepository {
    private lateinit var dao: DepositDao

    fun init(database: AppDatabase) {
        dao = database.depositDao()
    }

    fun getAllCalculations(): Flow<List<DepositCalculation>> = dao.getAllCalculations()

    suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation)
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return dao.getCalculationById(id)
    }
}