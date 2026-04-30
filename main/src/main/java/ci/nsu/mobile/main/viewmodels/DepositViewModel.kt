package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow

class DepositViewModel(application: Application) : AndroidViewModel(application) {
    // Input states
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")
    var selectedRate by mutableStateOf(0.0)

    // UI states
    var validationError by mutableStateOf<String?>(null)
    var availableRates by mutableStateOf(listOf<Double>())
    var calculationResult by mutableStateOf<Pair<Double, Double>?>(null) // (finalAmount, interestEarned)

    // History
    private val _history = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val history: StateFlow<List<DepositCalculation>> = _history.asStateFlow()

    private val repository: DepositRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DepositRepository(database.depositDao())
        loadHistory()
    }

    // Update available rates based on period
    fun updateAvailableRates(periodMonthsStr: String) {
        val period = periodMonthsStr.toIntOrNull()
        availableRates = when {
            period == null -> emptyList()
            period < 6 -> listOf(15.0)
            period < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
        // Auto-select first available rate if current selection is invalid
        if (availableRates.isNotEmpty() && selectedRate !in availableRates) {
            selectedRate = availableRates.first()
        }
    }

    // Validate step 1
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

    // Validate step 2
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

    // Calculate result
    fun calculateResult() {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val rate = selectedRate
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        // Compound interest calculation with monthly top-ups
        val monthlyRate = rate / 100 / 12
        var finalAmount = amount
        for (i in 1..months) {
            finalAmount = finalAmount * (1 + monthlyRate) + topUp
        }
        val interestEarned = finalAmount - amount - (topUp * months)

        calculationResult = Pair(finalAmount, interestEarned)
    }

    // Save calculation to database
    fun saveCalculation(onSuccess: () -> Unit) {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val rate = selectedRate
        val topUp = monthlyTopUp.toDoubleOrNull()
        val (finalAmount, interestEarned) = calculationResult ?: return

        val calculation = DepositCalculation(
            initialAmount = amount,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.saveCalculation(calculation)
            loadHistory()
            clearForm()  // <- добавляем очистку формы
            onSuccess()
        }
    }

    // Новый метод для очистки формы
    fun clearForm() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = 0.0
        validationError = null
        calculationResult = null
        availableRates = emptyList()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { calculations ->
                _history.value = calculations
            }
        }
    }

    // Reset all inputs
    fun reset() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = 0.0
        validationError = null
        calculationResult = null
        availableRates = emptyList()
    }
}