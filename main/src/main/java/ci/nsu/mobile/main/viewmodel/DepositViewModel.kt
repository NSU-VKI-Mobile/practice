package ci.nsu.mobile.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.InterestRates
import ci.nsu.mobile.main.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _firstStepState = MutableStateFlow(FirstStepState())
    val firstStepState: StateFlow<FirstStepState> = _firstStepState.asStateFlow()

    private val _secondStepState = MutableStateFlow(SecondStepState())
    val secondStepState: StateFlow<SecondStepState> = _secondStepState.asStateFlow()

    private val _resultState = MutableStateFlow(ResultState())
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    private val _calculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val calculations: StateFlow<List<DepositCalculation>> = _calculations.asStateFlow()

    fun updateInitialAmount(value: String) {
        _firstStepState.update { current ->
            val error = if (value.isNotBlank()) {
                val amount = value.toDoubleOrNull()
                if (amount == null) "Введите число"
                else if (amount <= 0) "Сумма должна быть больше 0"
                else null
            } else "Введите сумму"

            current.copy(
                initialAmount = value,
                initialAmountError = error,
                isNextEnabled = error == null && current.periodMonthsError == null
            )
        }
    }

    fun updatePeriodMonths(value: String) {
        _firstStepState.update { current ->
            val error = if (value.isNotBlank()) {
                val months = value.toIntOrNull()
                if (months == null) "Введите число"
                else if (months <= 0) "Срок должен быть больше 0"
                else null
            } else "Введите срок"

            current.copy(
                periodMonths = value,
                periodMonthsError = error,
                isNextEnabled = current.initialAmountError == null && error == null
            )
        }
    }

    fun goToSecondStep() {
        val state = _firstStepState.value
        val amount = state.initialAmount.toDoubleOrNull() ?: 0.0
        val months = state.periodMonths.toIntOrNull() ?: 0


        val availableRates = InterestRates.getAvailableRates()
        val defaultRate = InterestRates.getDefaultRate(months)
        val defaultPeriod = InterestRates.getDefaultPeriod(months)

        _secondStepState.update {
            SecondStepState(
                initialAmount = amount,
                periodMonths = months,
                interestRate = defaultRate,
                availableRates = availableRates,
                selectedRate = defaultRate,
                selectedPeriodMonths = defaultPeriod
            )
        }
    }

    fun selectRate(rate: Double) {
        val period = InterestRates.getPeriodForRate(rate)

        _secondStepState.update { current ->
            current.copy(
                selectedRate = rate,
                selectedPeriodMonths = period
            )
        }
    }

    fun updateMonthlyTopUp(value: String) {
        _secondStepState.update { current ->
            val error = if (value.isNotBlank()) {
                val amount = value.toDoubleOrNull()
                if (amount == null) "Введите число"
                else if (amount < 0) "Сумма не может быть отрицательной"
                else null
            } else null

            current.copy(
                monthlyTopUp = value,
                monthlyTopUpError = error,
                isCalculateEnabled = error == null
            )
        }
    }

    fun calculateResult() {
        val state = _secondStepState.value
        val monthlyTopUpValue = state.monthlyTopUp.toDoubleOrNull()

        val months = state.selectedPeriodMonths
        val monthlyRate = state.selectedRate / 100 / 12

        val finalAmount = if (monthlyTopUpValue != null && monthlyTopUpValue > 0) {
            var total = state.initialAmount
            repeat(months) {
                total = total * (1 + monthlyRate) + monthlyTopUpValue
            }
            total
        } else {
            state.initialAmount * Math.pow(1 + monthlyRate, months.toDouble())
        }

        val totalTopUp = (monthlyTopUpValue ?: 0.0) * months
        val interestEarned = finalAmount - state.initialAmount - totalTopUp

        Log.d("DepositCalculation", "=== РАСЧЁТ ===")
        Log.d("DepositCalculation", "Начальная сумма: ${state.initialAmount}")
        Log.d("DepositCalculation", "Срок (мес): $months")
        Log.d("DepositCalculation", "Ставка: ${state.selectedRate}%")
        Log.d("DepositCalculation", "Месячная ставка: $monthlyRate")
        Log.d("DepositCalculation", "Пополнение: $monthlyTopUpValue")
        Log.d("DepositCalculation", "Итоговая сумма: $finalAmount")
        Log.d("DepositCalculation", "Начисленные проценты: $interestEarned")

        _resultState.update {
            ResultState(
                initialAmount = state.initialAmount,
                periodMonths = months,
                interestRate = state.selectedRate,
                monthlyTopUp = monthlyTopUpValue,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                showResult = true
            )
        }
    }

    fun saveCalculation() {
        val result = _resultState.value
        if (result.showResult) {
            val monthlyTopUpValue = result.monthlyTopUp

            val calculation = DepositCalculation(
                initialAmount = result.initialAmount,
                periodMonths = result.periodMonths,
                interestRate = result.interestRate,
                monthlyTopUp = monthlyTopUpValue,
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned
            )
            viewModelScope.launch {
                repository.saveCalculation(calculation)
                loadCalculations()
            }
        }
    }

    fun loadCalculations() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _calculations.value = list
            }
        }
    }

    fun reset() {
        _firstStepState.value = FirstStepState()
        _secondStepState.value = SecondStepState()
        _resultState.value = ResultState()
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadCalculations()
        }
    }
}