package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainInputViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    // Ключи для сохранения состояния
    private val KEY_START_AMOUNT = "startAmount"
    private val KEY_TERM_MONTHS = "termMonths"

    // Начальные значения из SavedStateHandle или пустые строки
    private val _startAmount = MutableStateFlow(savedStateHandle.get<String>(KEY_START_AMOUNT) ?: "")
    val startAmount: StateFlow<String> = _startAmount.asStateFlow()

    private val _termMonths = MutableStateFlow(savedStateHandle.get<String>(KEY_TERM_MONTHS) ?: "")
    val termMonths: StateFlow<String> = _termMonths.asStateFlow()

    fun updateStartAmount(value: String) {
        _startAmount.update { value }
        savedStateHandle[KEY_START_AMOUNT] = value
    }

    fun updateTermMonths(value: String) {
        _termMonths.update { value }
        savedStateHandle[KEY_TERM_MONTHS] = value
    }

    fun getStartAmountValue(): Double = _startAmount.value.toDoubleOrNull() ?: 0.0
    fun getTermMonthsValue(): Int = _termMonths.value.toIntOrNull() ?: 0
}