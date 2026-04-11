package ci.nsu.mobile.main

import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.DBO.Deposit
import ci.nsu.mobile.main.DBO.DepositDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DepositRepository(private val depositDao: DepositDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val depositList: LiveData<List<Deposit>> = depositDao.getDeposits()

    fun AddDeposit(deposit: Deposit) {
        coroutineScope.launch(Dispatchers.IO) {
            depositDao.addDeposit(deposit)
        }
    }
}