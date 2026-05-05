package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DepositViewModel : ViewModel() {

    private val _initialAmount = MutableStateFlow("")
    val initialAmount: StateFlow<String> = _initialAmount

    private val _months = MutableStateFlow("")
    val months: StateFlow<String> = _months

    private val _topUp = MutableStateFlow("")
    val topUp: StateFlow<String> = _topUp

    private val _rate = MutableStateFlow(0.0)
    val rate: StateFlow<Double> = _rate

    fun setInitialAmount(value: String) {
        _initialAmount.value = value
    }

    fun setMonths(value: String) {
        _months.value = value
    }

    fun setTopUp(value: String) {
        _topUp.value = value
    }

    fun calculateRate() {
        val m = _months.value.toIntOrNull() ?: return
        _rate.value = when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }
    }
}