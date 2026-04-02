package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DepositViewModel : ViewModel() {

    private val _initialAmount = MutableStateFlow("")
    val initialAmount: StateFlow<String> = _initialAmount.asStateFlow()

    private val _periodMonths = MutableStateFlow("")
    val periodMonths: StateFlow<String> = _periodMonths.asStateFlow()


    private val _interestRate = MutableStateFlow<Double?>(null)
    val interestRate: StateFlow<Double?> = _interestRate.asStateFlow()

    private val _monthlyTopUp = MutableStateFlow<String>("")
    val monthlyTopUp: StateFlow<String> = _monthlyTopUp.asStateFlow()


    fun saveFirstScreenData(amount: String, months: String) {
        _initialAmount.value = amount
        _periodMonths.value = months
    }

    fun saveSecondScreenData(rate: Double, topUp: String) {
        _interestRate.value = rate
        _monthlyTopUp.value = topUp
    }

    fun getInitialAmount(): String = _initialAmount.value
    fun getPeriodMonths(): String = _periodMonths.value
    fun getInterestRate(): Double? = _interestRate.value
    fun getMonthlyTopUp(): String = _monthlyTopUp.value
}