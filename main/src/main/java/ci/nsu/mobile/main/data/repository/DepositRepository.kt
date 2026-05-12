package ci.nsu.mobile.main.data.repository

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.data.entity.Deposit
import ci.nsu.mobile.main.data.entity.DepositDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepositRepository(private val depositDao: DepositDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val depositList: LiveData<List<Deposit>> = depositDao.getDeposits()

    fun addDeposit(deposit: Deposit) {
        coroutineScope.launch(Dispatchers.IO) {
            depositDao.addDeposit(deposit)
        }
    }
}