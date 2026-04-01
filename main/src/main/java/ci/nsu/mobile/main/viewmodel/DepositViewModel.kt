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

    fun saveFirstScreenData(amount: String, months: String) {
        _initialAmount.value = amount
        _periodMonths.value = months
    }

    fun getInitialAmount(): String = _initialAmount.value
    fun getPeriodMonths(): String = _periodMonths.value
}