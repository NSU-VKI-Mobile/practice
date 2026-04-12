package ci.nsu.mobile.main.data.repository

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.DepositCalculation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DepositRepository(private val database: AppDatabase) {
    private val dao = database.depositDao()

    suspend fun insertDeposit(deposit: DepositCalculation) {
        withContext(Dispatchers.IO) {
            dao.insert(deposit)
        }
    }

    fun getAllDeposits(): LiveData<List<DepositCalculation>> {
        return dao.getAllDeposits()
    }

    suspend fun getDepositById(id: Long): DepositCalculation? {
        return withContext(Dispatchers.IO) {
            dao.getDepositById(id)
        }
    }
}