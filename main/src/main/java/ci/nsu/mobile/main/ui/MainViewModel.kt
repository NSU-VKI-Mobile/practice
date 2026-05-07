package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Состояние ввода данных
data class DepositInput(
    val initialAmount: Double? = null,
    val periodMonths: Int? = null,
    val monthlyTopUp: Double? = null
)

// Результат расчёта
data class CalculationResult(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double
)

class MainViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _input = MutableStateFlow(DepositInput())
    val input: StateFlow<DepositInput> = _input.asStateFlow()

    private val _result = MutableStateFlow<CalculationResult?>(null)
    val result: StateFlow<CalculationResult?> = _result.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun updateInitialAmount(amount: Double?) {
        _input.value = _input.value.copy(initialAmount = amount)
        if (amount != null && amount <= 0) {
            _error.value = "Стартовый взнос должен быть больше 0"
        } else {
            _error.value = null
        }
    }

    fun updatePeriodMonths(period: Int?) {
        _input.value = _input.value.copy(periodMonths = period)
        if (period != null && period <= 0) {
            _error.value = "Срок должен быть больше 0 месяцев"
        } else {
            _error.value = null
        }
    }

    fun updateMonthlyTopUp(amount: Double?) {
        _input.value = _input.value.copy(monthlyTopUp = amount)
    }

    fun getInterestRate(periodMonths: Int?): Double {
        return when {
            periodMonths == null || periodMonths <= 0 -> 0.0
            periodMonths < 6 -> 0.15
            periodMonths < 12 -> 0.10
            else -> 0.05
        }
    }

    fun calculateResult(): Boolean {
        val input = _input.value
        if (input.initialAmount == null || input.initialAmount <= 0) {
            _error.value = "Введите корректный стартовый взнос"
            return false
        }
        if (input.periodMonths == null || input.periodMonths <= 0) {
            _error.value = "Введите корректный срок вклада"
            return false
        }

        val rate = getInterestRate(input.periodMonths)
        val monthlyRate = rate / 12
        var amount = input.initialAmount

        // Расчёт с капитализацией и пополнениями
        for (month in 1..input.periodMonths) {
            amount += amount * monthlyRate
            input.monthlyTopUp?.let { amount += it }
        }

        val interestEarned = amount - input.initialAmount - (input.monthlyTopUp?.times(input.periodMonths) ?: 0.0)

        _result.value = CalculationResult(
            initialAmount = input.initialAmount,
            periodMonths = input.periodMonths,
            interestRate = rate,
            monthlyTopUp = input.monthlyTopUp,
            finalAmount = amount,
            interestEarned = interestEarned
        )
        _error.value = null
        return true
    }

    fun saveCalculation() {
        val result = _result.value ?: return
        viewModelScope.launch {
            val calculation = DepositCalculation(
                initialAmount = result.initialAmount,
                periodMonths = result.periodMonths,
                interestRate = result.interestRate,
                monthlyTopUp = result.monthlyTopUp,
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned
            )
            repository.insert(calculation)
        }
    }

    fun clearInput() {
        _input.value = DepositInput()
        _result.value = null
        _error.value = null
    }
}