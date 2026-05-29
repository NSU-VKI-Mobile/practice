package ci.nsu.mobile.main.data

import androidx.lifecycle.LiveData

class DepositRepository(private val depositDao: DepositDao) {
    val allHistory: LiveData<List<DepositCalculation>> = depositDao.getAllHistory()

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insert(calculation)
    }
}