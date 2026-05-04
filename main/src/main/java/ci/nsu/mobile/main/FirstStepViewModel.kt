package ci.nsu.mobile.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FirstStepViewModel : ViewModel() {
    private val _initialAmount = mutableStateOf("")
    private val _periodMonths = mutableStateOf("")
    private val _initialAmountError = mutableStateOf<String?>(null)
    private val _periodError = mutableStateOf<String?>(null)

    val initialAmount: String get() = _initialAmount.value
    val periodMonths: String get() = _periodMonths.value
    val initialAmountError: String? get() = _initialAmountError.value
    val periodError: String? get() = _periodError.value
    val isNextEnabled: Boolean get() = initialAmountError == null && periodError == null &&
            initialAmount.isNotEmpty() && periodMonths.isNotEmpty()

    fun updateInitialAmount(value: String) {
        _initialAmount.value = value
        val amount = value.toDoubleOrNull()
        _initialAmountError.value = when {
            value.isEmpty() -> "Поле обязательно"
            amount == null -> "Введите число"
            amount <= 0 -> "Сумма должна быть больше 0"
            else -> null
        }
    }

    fun updatePeriodMonths(value: String) {
        _periodMonths.value = value
        val months = value.toIntOrNull()
        _periodError.value = when {
            value.isEmpty() -> "Поле обязательно"
            months == null -> "Введите число"
            months <= 0 -> "Срок должен быть больше 0"
            else -> null
        }
    }

    fun getData(): Pair<Double, Int> {
        return Pair(initialAmount.toDoubleOrNull() ?: 0.0, periodMonths.toIntOrNull() ?: 0)
    }
    fun resetData() {
        _initialAmount.value = ""
        _periodMonths.value = ""
        _initialAmountError.value = null
        _periodError.value = null
    }
}

