package ci.nsu.mobile.main.Database

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.Database.DepositDao
import ci.nsu.mobile.main.Database.DepositCalculation

class DepositRepository(private val dao: DepositDao) {
    val allCalculations: LiveData<List<DepositCalculation>> = dao.getAllDeposits()

    suspend fun insert(calculation: DepositCalculation) = dao.insert(calculation)
}