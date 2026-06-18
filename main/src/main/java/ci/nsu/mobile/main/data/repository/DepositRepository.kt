package ci.nsu.mobile.main.data.repository

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.data.dao.DepositDao
import ci.nsu.mobile.main.data.entity.DepositCalculation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DepositRepository(private val dao: DepositDao) {

    suspend fun insert(calculation: DepositCalculation): Long {
        return withContext(Dispatchers.IO) {
            dao.insert(calculation)
        }
    }

    fun getAllCalculations(): LiveData<List<DepositCalculation>> {
        return dao.getAllCalculations()
    }

    fun getById(id: Long): LiveData<DepositCalculation?> {
        return dao.getById(id)
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }
}
