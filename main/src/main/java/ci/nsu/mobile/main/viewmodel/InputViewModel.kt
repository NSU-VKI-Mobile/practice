package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InputViewModel : ViewModel() {
    private val _initialAmount = MutableStateFlow(0.0)
    val initialAmount: StateFlow<Double> = _initialAmount.asStateFlow()

    private val _periodMonths = MutableStateFlow(0)
    val periodMonths: StateFlow<Int> = _periodMonths.asStateFlow()

    private val _initialAmountError = MutableStateFlow<String?>(null)
    val initialAmountError: StateFlow<String?> = _initialAmountError.asStateFlow()

    private val _periodError = MutableStateFlow<String?>(null)
    val periodError: StateFlow<String?> = _periodError.asStateFlow()

    private val _isValid = MutableStateFlow(false)
    val isValid: StateFlow<Boolean> = _isValid.asStateFlow()

    fun updateInitialAmount(value: String) {
        val amount = value.toDoubleOrNull() ?: 0.0
        _initialAmount.value = amount
        _initialAmountError.value = if (amount <= 0) "Сумма должна быть больше 0" else null
        validate()
    }

    fun updatePeriodMonths(value: String) {
        val months = value.toIntOrNull() ?: 0
        _periodMonths.value = months
        _periodError.value = if (months <= 0) "Срок должен быть больше 0 месяцев" else null
        validate()
    }

    private fun validate() {
        _isValid.value = _initialAmount.value > 0 && _periodMonths.value > 0
    }
}