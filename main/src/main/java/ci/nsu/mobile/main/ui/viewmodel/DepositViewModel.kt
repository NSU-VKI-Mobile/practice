package ci.nsu.mobile.main.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.token.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository, tokenManager: TokenManager) : ViewModel() {

    // Входные состояния
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")
    var selectedRate by mutableStateOf(0.0)

    // UI состояния
    var validationError by mutableStateOf<String?>(null)
    var availableRates by mutableStateOf(listOf<Double>())
    var calculationResult by mutableStateOf<Pair<Double, Double>?>(null)

    // История расчетов
    private val _history = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val history: StateFlow<List<DepositCalculation>> = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _history.value = list
            }
        }
    }

    fun updateAvailableRates(periodMonthsStr: String) {
        val period = periodMonthsStr.toIntOrNull()
        availableRates = when {
            period == null -> emptyList()
            period < 6 -> listOf(15.0)
            period < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
        if (availableRates.isNotEmpty() && selectedRate !in availableRates) {
            selectedRate = availableRates.first()
        }
    }

    fun validateStep1(): Boolean {
        return when {
            initialAmount.isBlank() -> {
                validationError = "Введите стартовый взнос"
                false
            }
            initialAmount.toDoubleOrNull() == null || initialAmount.toDouble() <= 0 -> {
                validationError = "Введите корректную сумму стартового взноса"
                false
            }
            periodMonths.isBlank() -> {
                validationError = "Введите срок вклада"
                false
            }
            periodMonths.toIntOrNull() == null || periodMonths.toInt() <= 0 -> {
                validationError = "Введите корректный срок вклада (месяцы)"
                false
            }
            else -> {
                validationError = null
                true
            }
        }
    }

    fun validateStep2(): Boolean {
        val period = periodMonths.toIntOrNull()
        return when {
            period == null -> {
                validationError = "Срок вклада не указан. Вернитесь на первый шаг"
                false
            }
            selectedRate == 0.0 -> {
                validationError = "Выберите процентную ставку"
                false
            }
            else -> {
                validationError = null
                true
            }
        }
    }

    fun calculateResult() {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val rate = selectedRate
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val monthlyRate = rate / 100 / 12
        var finalAmount = amount
        for (i in 1..months) {
            finalAmount = finalAmount * (1 + monthlyRate) + topUp
        }
        val interestEarned = finalAmount - amount - (topUp * months)

        calculationResult = Pair(finalAmount, interestEarned)
    }

    fun saveCalculation(onSuccess: () -> Unit) {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val rate = selectedRate
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0
        val result = calculationResult ?: return

        viewModelScope.launch {
            val calculation = DepositCalculation(
                initialAmount = amount,
                periodMonths = months,
                interestRate = rate,
                monthlyTopUp = topUp,
                finalAmount = result.first,
                interestEarned = result.second,
                calculationDate = System.currentTimeMillis()
            )
            repository.saveCalculation(calculation)
            onSuccess()
        }
    }

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
        }
    }

    fun reset() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = 0.0
        validationError = null
        calculationResult = null
    }
}