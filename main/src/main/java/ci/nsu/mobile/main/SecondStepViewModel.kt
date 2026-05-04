package ci.nsu.mobile.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SecondStepViewModel : ViewModel() {
    private val _availableRates = mutableStateOf<List<Double>>(emptyList())
    private val _selectedRate = mutableStateOf<Double?>(null)
    private val _monthlyTopUp = mutableStateOf("")
    private val _errorMessage = mutableStateOf<String?>(null)

    val availableRates: List<Double> get() = _availableRates.value
    val selectedRate: Double? get() = _selectedRate.value
    val monthlyTopUp: String get() = _monthlyTopUp.value
    val errorMessage: String? get() = _errorMessage.value
    val isCalculateEnabled: Boolean get() = availableRates.isNotEmpty()

    fun updatePeriodMonths(periodMonths: Int) {
        _availableRates.value = when {
            periodMonths <= 0 -> emptyList()
            periodMonths < 6 -> listOf(15.0)
            periodMonths < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
        _selectedRate.value = _availableRates.value.firstOrNull()
        _errorMessage.value = if (periodMonths <= 0) "Некорректный срок вклада" else null
    }

    fun selectRate(rate: Double) {
        _selectedRate.value = rate
    }

    fun updateMonthlyTopUp(value: String) {
        _monthlyTopUp.value = value
    }

    fun getData(): Pair<Double, Double> {
        return Pair(selectedRate ?: 0.0, monthlyTopUp.toDoubleOrNull() ?: 0.0)
    }
    fun resetData() {
        _monthlyTopUp.value = ""
    }
}

