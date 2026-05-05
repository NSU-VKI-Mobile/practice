package mobile.domain
import mobile.data.DepositCalculation
import mobile.data.DepositDao
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    val allCalculations: Flow<List<DepositCalculation>> = depositDao.getAllCalculations()

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insertCalculation(calculation)
    }
}