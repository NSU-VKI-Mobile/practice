package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    val allCalculations: LiveData<List<DepositCalculation>> = repository.allCalculations
    private val _step1Data = MutableLiveData<Pair<Double, Int>>()
    val step1Data: LiveData<Pair<Double, Int>> = _step1Data

    fun setStep1Data(initialAmount: Double, periodMonths: Int) {
        _step1Data.value = Pair(initialAmount, periodMonths)
    }
    private val _currentCalculation = MutableLiveData<DepositCalculation?>()
    val currentCalculation: LiveData<DepositCalculation?> = _currentCalculation

    fun calculate(initial: Double, months: Int, rate: Double, monthlyTopUp: Double?) {
        var amount = initial
        val monthlyRate = rate / 100.0 / 12

        for (i in 1..months) {
            amount += amount * monthlyRate
            monthlyTopUp?.let { amount += it }
        }

        val totalTopUp = (monthlyTopUp ?: 0.0) * months
        val interest = amount - initial - totalTopUp

        _currentCalculation.value = DepositCalculation(
            initialAmount = initial,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = amount,
            interestEarned = interest
        )
    }

    fun saveCurrent() {
        _currentCalculation.value?.let {
            viewModelScope.launch {
                repository.insert(it)
            }
        }
    }
}