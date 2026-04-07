package ci.nsu.mobile.main.data.repository

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.data.dao.DepositDao
import ci.nsu.mobile.main.data.model.DepositCalculation

class DepositRepository(private val dao: DepositDao) {
    val allCalculations: LiveData<List<DepositCalculation>> = dao.getAll()

    suspend fun insert(calculation: DepositCalculation) = dao.insert(calculation)
}