package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainInputViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val KEY_START_AMOUNT = "startAmount"
    private val KEY_TERM_MONTHS = "termMonths"

    private val _startAmount =
        MutableStateFlow(savedStateHandle.get<String>(KEY_START_AMOUNT) ?: "")
    val startAmount: StateFlow<String> = _startAmount.asStateFlow()

    private val _termMonths = MutableStateFlow(savedStateHandle.get<String>(KEY_TERM_MONTHS) ?: "")
    val termMonths: StateFlow<String> = _termMonths.asStateFlow()

    fun updateStartAmount(newValue: String) {
        if (isValidStartAmount(newValue)) {
            _startAmount.update { newValue }
            savedStateHandle[KEY_START_AMOUNT] = newValue
        }
    }

    fun updateTermMonths(newValue: String) {
        if (isValidTermMonths(newValue)) {
            _termMonths.update { newValue }
            savedStateHandle[KEY_TERM_MONTHS] = newValue
        }
    }

    fun getStartAmountValue(): Double = _startAmount.value.toDoubleOrNull() ?: 0.0
    fun getTermMonthsValue(): Int = _termMonths.value.toIntOrNull() ?: 0

    private fun isValidStartAmount(value: String): Boolean {
        if (value.isEmpty()) return true
        // Разрешаем: цифры, одна точка, и точка не в начале
        val regex = Regex("^\\d+(\\.\\d*)?$")
        return regex.matches(value)
    }

    private fun isValidTermMonths(value: String): Boolean {
        return value.isEmpty() || value.all { it.isDigit() }
    }
}