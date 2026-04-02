package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.repositories.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _initialAmount = MutableStateFlow("")
    val initialAmount: StateFlow<String> = _initialAmount.asStateFlow()

    private val _periodMonths = MutableStateFlow("")
    val periodMonths: StateFlow<String> = _periodMonths.asStateFlow()

    fun saveFirstScreenData(amount: String, months: String) {
        _initialAmount.value = amount
        _periodMonths.value = months
    }


    private val _interestRate = MutableStateFlow<Double?>(null)
    val interestRate: StateFlow<Double?> = _interestRate.asStateFlow()

    private val _monthlyTopUp = MutableStateFlow<String>("")
    val monthlyTopUp: StateFlow<String> = _monthlyTopUp.asStateFlow()


    private val _finalAmount = MutableStateFlow(0.0)
    val finalAmount: StateFlow<Double> = _finalAmount.asStateFlow()

    private val _interestEarned = MutableStateFlow(0.0)
    val interestEarned: StateFlow<Double> = _interestEarned.asStateFlow()

    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()

    fun saveSecondScreenData(rate: Double, topUp: String) {
        _interestRate.value = rate
        _monthlyTopUp.value = topUp
        calculateResult()
    }

    private fun calculateResult() {
        val initial = _initialAmount.value.toDoubleOrNull() ?: return
        val months = _periodMonths.value.toIntOrNull() ?: return
        val rate = _interestRate.value ?: return
        val topUp = _monthlyTopUp.value.toDoubleOrNull() ?: 0.0

        val monthlyRate = rate / 100 / 12
        var finalAmount = initial
        var totalInterest = 0.0

        for (month in 1..months) {
            val interest = finalAmount * monthlyRate
            finalAmount += interest
            totalInterest += interest

            finalAmount += topUp
        }

        _finalAmount.value = finalAmount
        _interestEarned.value = totalInterest
    }



    fun getInitialAmount(): String = _initialAmount.value
    fun getPeriodMonths(): String = _periodMonths.value
    fun getInterestRate(): Double? = _interestRate.value
    fun getMonthlyTopUp(): String = _monthlyTopUp.value
    fun getFinalAmount(): Double = _finalAmount.value
    fun getInterestEarned(): Double = _interestEarned.value
}