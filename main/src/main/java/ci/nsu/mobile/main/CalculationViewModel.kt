package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculationViewModel : ViewModel() {

    private val _startAmountInput = MutableStateFlow("")
    val startAmountInput: StateFlow<String> = _startAmountInput.asStateFlow()

    private val _termMonthsInput = MutableStateFlow("")
    val termMonthsInput: StateFlow<String> = _termMonthsInput.asStateFlow()

    private val _startAmountError = MutableStateFlow<String?>(null)
    val startAmountError: StateFlow<String?> = _startAmountError.asStateFlow()

    private val _termMonthsError = MutableStateFlow<String?>(null)
    val termMonthsError: StateFlow<String?> = _termMonthsError.asStateFlow()

    private val _monthlyDepositInput = MutableStateFlow("")
    val monthlyDepositInput: StateFlow<String> = _monthlyDepositInput.asStateFlow()

    private val _monthlyDepositError = MutableStateFlow<String?>(null)
    val monthlyDepositError: StateFlow<String?> = _monthlyDepositError.asStateFlow()

    private val _calculations = MutableStateFlow<List<Calculation>>(emptyList())
    val calculations: StateFlow<List<Calculation>> = _calculations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // валидация для экрана 1

    fun updateStartAmount(value: String) {
        _startAmountInput.value = value
        validateStartAmount(value)
    }

    fun updateTermMonths(value: String) {
        _termMonthsInput.value = value
        validateTermMonths(value)
    }

    private fun validateStartAmount(value: String) {
        when {
            value.isEmpty() -> {
                _startAmountError.value = "Поле обязательно для заполнения"
            }
            !value.matches(Regex("^\\d+(\\.\\d+)?$")) -> {
                _startAmountError.value = "Введите число (например: 1000 или 1000.50)"
            }
            value.toDoubleOrNull() == null -> {
                _startAmountError.value = "Некорректное число"
            }
            value.toDouble() <= 0 -> {
                _startAmountError.value = "Сумма должна быть больше 0"
            }
            value.toDouble() > 100_000_000 -> {
                _startAmountError.value = "Сумма не может превышать 100 000 000 ₽"
            }
            else -> {
                _startAmountError.value = null
            }
        }
    }

    private fun validateTermMonths(value: String) {
        when {
            value.isEmpty() -> {
                _termMonthsError.value = "Поле обязательно для заполнения"
            }
            !value.matches(Regex("^\\d+$")) -> {
                _termMonthsError.value = "Введите целое число (количество месяцев)"
            }
            value.toIntOrNull() == null -> {
                _termMonthsError.value = "Некорректное число"
            }
            value.toInt() <= 0 -> {
                _termMonthsError.value = "Срок должен быть больше 0 месяцев"
            }
            value.toInt() > 600 -> {
                _termMonthsError.value = "Срок не может превышать 600 месяцев (50 лет)"
            }
            else -> {
                _termMonthsError.value = null
            }
        }
    }

    fun validateStep1(): Boolean {
        validateStartAmount(_startAmountInput.value)
        validateTermMonths(_termMonthsInput.value)
        return _startAmountError.value == null && _termMonthsError.value == null
    }

    fun getValidatedStartAmount(): Double? {
        return if (_startAmountError.value == null && _startAmountInput.value.isNotEmpty()) {
            _startAmountInput.value.toDoubleOrNull()
        } else null
    }

    fun getValidatedTermMonths(): Int? {
        return if (_termMonthsError.value == null && _termMonthsInput.value.isNotEmpty()) {
            _termMonthsInput.value.toIntOrNull()
        } else null
    }

    fun updateMonthlyDeposit(value: String) {
        _monthlyDepositInput.value = value
        validateMonthlyDeposit(value)
    }
//валидация для экрана 2
    private fun validateMonthlyDeposit(value: String) {
        when {
            value.isEmpty() -> {
                _monthlyDepositError.value = null // Необязательное поле
            }
            !value.matches(Regex("^\\d+(\\.\\d+)?$")) -> {
                _monthlyDepositError.value = "Введите число (например: 1000 или 1000.50)"
            }
            value.toDoubleOrNull() == null -> {
                _monthlyDepositError.value = "Некорректное число"
            }
            value.toDouble() < 0 -> {
                _monthlyDepositError.value = "Сумма не может быть отрицательной"
            }
            value.toDouble() > 10_000_000 -> {
                _monthlyDepositError.value = "Сумма не может превышать 10 000 000 ₽"
            }
            else -> {
                _monthlyDepositError.value = null
            }
        }
    }

    fun getValidatedMonthlyDeposit(): Double {
        return if (_monthlyDepositError.value == null && _monthlyDepositInput.value.isNotEmpty()) {
            _monthlyDepositInput.value.toDoubleOrNull() ?: 0.0
        } else 0.0
    }

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val database = AppDatabase.getDatabase(ApplicationClass.getAppContext())
                val calculationsList = database.calculationDao().getAllCalculationsList()
                _calculations.value = calculationsList
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveCalculation(calculation: Calculation) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val database = AppDatabase.getDatabase(ApplicationClass.getAppContext())
                database.calculationDao().insertCalculation(calculation)
                _saveSuccess.value = true
                loadCalculations()
                kotlinx.coroutines.delay(2000)
                _saveSuccess.value = false
            } catch (e: Exception) {
                _error.value = "Ошибка сохранения: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCalculation(calculation: Calculation) {
        viewModelScope.launch {
            try {
                val database = AppDatabase.getDatabase(ApplicationClass.getAppContext())
                database.calculationDao().deleteCalculation(calculation)
                loadCalculations()
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}"
            }
        }
    }


    fun calculateResult(
        startAmount: Double,
        termMonths: Int,
        interestRate: Double,
        monthlyDeposit: Double
    ): Pair<Double, Double> {
        val monthlyRate = interestRate / 100 / 12
        var totalAmount = startAmount

        for (i in 1..termMonths) {
            totalAmount += totalAmount * monthlyRate
            totalAmount += monthlyDeposit
        }

        val totalProfit = totalAmount - startAmount - (monthlyDeposit * termMonths)
        return Pair(totalAmount, totalProfit)
    }


    fun getAvailableRates(termMonths: Int): List<InterestRate> {
        return listOf(
            InterestRate(12.0, "12% (для вкладов до 6 месяцев)", termMonths < 6 && termMonths > 0),
            InterestRate(10.0, "10% (для вкладов от 6 до 12 месяцев)", termMonths in 6..11),
            InterestRate(5.0, "5% (для вкладов от 12 месяцев)", termMonths >= 12)
        )
    }

    fun clearError() {
        _error.value = null
    }

    fun resetFields() {
        _startAmountInput.value = ""
        _termMonthsInput.value = ""
        _monthlyDepositInput.value = ""
        _startAmountError.value = null
        _termMonthsError.value = null
        _monthlyDepositError.value = null
    }
}

data class InterestRate(
    val rate: Double,
    val description: String,
    val isAvailable: Boolean
)