package com.example.calculations.data.repository

import androidx.lifecycle.LiveData
import com.example.calculations.data.dba.Deposit
import com.example.calculations.data.dba.DepositDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DepositRepository(private val depositDao: DepositDao) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val depositList: LiveData<List<Deposit>> = depositDao.getDeposits()

    fun addDeposit(deposit: Deposit) {
        coroutineScope.launch(Dispatchers.IO) {
            depositDao.addDeposit(deposit)
        }
    }

    fun deleteDeposit(depositId: Long){
        coroutineScope.launch(Dispatchers.IO) {
            depositDao.deleteById(depositId)
        }
    }

    fun getDepositUser(userId: Long) : Flow<List<Deposit>> {
        return depositDao.getDepositsUser(userId)
    }
}