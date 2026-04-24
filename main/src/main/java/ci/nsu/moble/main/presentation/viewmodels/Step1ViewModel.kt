package ci.nsu.moble.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Step1State(
    val amount: String = "",
    val months: String = "",
    val amountError: Boolean = false,
    val monthsError: Boolean = false
)

class Step1ViewModel : ViewModel() {
    private val _state = MutableStateFlow(Step1State())
    val state: StateFlow<Step1State> = _state.asStateFlow()

    fun setAmount(value: String) {
        _state.update {
            it.copy(
                amount = value,
                amountError = value.toDoubleOrNull()?.let { it <= 0 } ?: true
            )
        }
    }

    fun setMonths(value: String) {
        _state.update {
            it.copy(
                months = value,
                monthsError = value.toIntOrNull()?.let { it <= 0 } ?: true
            )
        }
    }

    fun getValidatedData(): Pair<Double, Int>? {
        val current = _state.value
        val amount = current.amount.toDoubleOrNull()
        val months = current.months.toIntOrNull()
        if (amount != null && amount > 0 && months != null && months > 0) {
            return Pair(amount, months)
        }
        _state.update {
            it.copy(
                amountError = amount == null || amount <= 0,
                monthsError = months == null || months <= 0
            )
        }
        return null
    }
}