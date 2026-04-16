package ci.nsu.mobile.main.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class StepOneState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val initialAmountError: String? = null,
    val periodMonthsError: String? = null
)

class StepOneViewModel : ViewModel() {
    private val _state = MutableStateFlow(StepOneState())
    val state: StateFlow<StepOneState> = _state.asStateFlow()

    fun updateInitialAmount(value: String) {
        _state.update {
            it.copy(
                initialAmount = value,
                initialAmountError = validateInitialAmount(value)
            )
        }
    }

    fun updatePeriodMonths(value: String) {
        _state.update {
            it.copy(
                periodMonths = value,
                periodMonthsError = validatePeriodMonths(value)
            )
        }
    }

    private fun validateInitialAmount(value: String): String? {
        if (value.isBlank()) return "Обязательное поле"
        val amount = value.toDoubleOrNull()
        if (amount == null) return "Введите корректное число"
        if (amount <= 0) return "Сумма должна быть больше 0"
        if (amount > 100_000_000) return "Сумма слишком велика"
        return null
    }

    private fun validatePeriodMonths(value: String): String? {
        if (value.isBlank()) return "Обязательное поле"
        val months = value.toIntOrNull()
        if (months == null) return "Введите целое число"
        if (months <= 0) return "Срок должен быть больше 0"
        if (months > 600) return "Срок слишком большой"
        return null
    }

    fun isValid(): Boolean {
        return _state.value.initialAmountError == null &&
                _state.value.periodMonthsError == null &&
                _state.value.initialAmount.isNotBlank() &&
                _state.value.periodMonths.isNotBlank()
    }

    fun getInitialAmount(): Double = _state.value.initialAmount.toDouble()
    fun getPeriodMonths(): Int = _state.value.periodMonths.toInt()
}