package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.DepositCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculationViewModel(
    private val initialAmount: Double,
    private val periodMonths: Int
) : ViewModel() {

    private val _availableRates = MutableStateFlow<List<Double>>(emptyList())
    val availableRates: StateFlow<List<Double>> = _availableRates.asStateFlow()

    private val _selectedRate = MutableStateFlow(0.0)
    val selectedRate: StateFlow<Double> = _selectedRate.asStateFlow()

    private val _monthlyTopUp = MutableStateFlow("")
    val monthlyTopUp: StateFlow<String> = _monthlyTopUp.asStateFlow()

    private val _rateError = MutableStateFlow<String?>(null)
    val rateError: StateFlow<String?> = _rateError.asStateFlow()

    private val _periodError = MutableStateFlow<String?>(null)
    val periodError: StateFlow<String?> = _periodError.asStateFlow()

    init {
        updateAvailableRates(periodMonths)
        if (periodMonths <= 0) {
            _periodError.value = "Срок вклада не указан или указан некорректно"
        }
    }

    private fun updateAvailableRates(months: Int) {
        val rates = when {
            months <= 0 -> emptyList()
            months < 6 -> listOf(15.0)
            months < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
        _availableRates.value = rates
        if (rates.isNotEmpty()) {
            _selectedRate.value = rates.first()
            _rateError.value = null
        } else {
            _rateError.value = "Нет доступных ставок для указанного срока"
        }
    }

    fun updateSelectedRate(rate: Double) {
        _selectedRate.value = rate
        _rateError.value = null
    }

    fun updateMonthlyTopUp(value: String) {
        _monthlyTopUp.value = value
    }

    fun calculateDeposit(): DepositCalculation? {
        if (periodMonths <= 0) {
            _periodError.value = "Срок вклада не указан"
            return null
        }

        if (_availableRates.value.isEmpty()) {
            _rateError.value = "Нет доступных ставок для указанного срока"
            return null
        }

        val monthlyTopUpValue = _monthlyTopUp.value.toDoubleOrNull()
        val monthlyRate = _selectedRate.value / 100 / 12
        var currentAmount = initialAmount
        var totalInterest = 0.0

        for (month in 1..periodMonths) {
            val interest = currentAmount * monthlyRate
            totalInterest += interest
            currentAmount += interest

            if (monthlyTopUpValue != null && monthlyTopUpValue > 0) {
                currentAmount += monthlyTopUpValue
            }
        }

        return DepositCalculation(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = _selectedRate.value,
            monthlyTopUp = monthlyTopUpValue,
            finalAmount = currentAmount,
            interestEarned = totalInterest,
            calculationDate = System.currentTimeMillis()
        )
    }
}