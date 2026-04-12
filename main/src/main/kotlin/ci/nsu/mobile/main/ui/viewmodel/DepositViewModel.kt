package ci.nsu.mobile.main.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {
    private val _repository: DepositRepository
    val allDeposits: LiveData<List<DepositCalculation>>

    var initialAmount: Double = 0.0
    var periodMonths: Int = 0
    var interestRate: Double = 0.0
    var monthlyTopUp: Double? = null
    var finalAmount: Double = 0.0
    var interestEarned: Double = 0.0

    init {
        val database = AppDatabase.getDatabase(application)
        _repository = DepositRepository(database)
        allDeposits = _repository.getAllDeposits()
    }

    val repository: DepositRepository
        get() = _repository

    fun calculateInterest() {
        var total = initialAmount
        var monthlyAddition = monthlyTopUp ?: 0.0
        val monthlyRate = interestRate / 100 / 12

        for (month in 1..periodMonths) {
            total += monthlyAddition
            total += total * monthlyRate
        }

        finalAmount = total
        interestEarned = total - initialAmount - (monthlyAddition * periodMonths)
    }

    fun saveCalculation() {
        viewModelScope.launch {
            val calculation = DepositCalculation(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                calculationDate = System.currentTimeMillis()
            )
            _repository.insertDeposit(calculation)
        }
    }

    fun getInterestRateForPeriod(): Double? {
        return when {
            periodMonths <= 0 -> null
            periodMonths < 6 -> 15.0
            periodMonths < 12 -> 10.0
            else -> 5.0
        }
    }
}