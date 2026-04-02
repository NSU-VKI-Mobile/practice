package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
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

    // В классе DepositViewModel, замените метод goToSecondStep и добавьте новые методы

    fun goToSecondStep() {
        val state = _firstStepState.value
        val amount = state.initialAmount.toDoubleOrNull() ?: 0.0
        val months = state.periodMonths.toIntOrNull() ?: 0

        // Создаём список доступных ставок с соответствующими сроками
        // Каждая ставка соответствует определённому диапазону сроков
        val availableRates = mutableListOf<Pair<Double, Int>>()

        if (months > 0) {
            // Добавляем все возможные варианты
            availableRates.add(15.0 to 5)   // 15% для срока 5 месяцев
            availableRates.add(10.0 to 9)   // 10% для срока 9 месяцев
            availableRates.add(5.0 to 12)   // 5% для срока 12 месяцев
        }

        // Находим ставку, соответствующую введённому сроку
        val defaultRate = when {
            months < 6 -> 15.0
            months < 12 -> 10.0
            else -> 5.0
        }

        val defaultPeriod = when {
            months < 6 -> 5
            months < 12 -> 9
            else -> 12
        }

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
        // Находим срок, соответствующий выбранной ставке
        val period = when (rate) {
            15.0 -> 5   // 15% → 5 месяцев
            10.0 -> 9   // 10% → 9 месяцев
            5.0 -> 12   // 5% → 12 месяцев
            else -> _secondStepState.value.selectedPeriodMonths
        }

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
        val monthlyTopUp = state.monthlyTopUp.toDoubleOrNull()

        // Используем выбранный срок (соответствующий ставке)
        val months = state.selectedPeriodMonths
        val monthlyRate = state.selectedRate / 100 / 12

        val finalAmount = if (monthlyTopUp != null && monthlyTopUp > 0) {
            // Формула с ежемесячным пополнением
            var total = state.initialAmount
            repeat(months) {
                total = total * (1 + monthlyRate) + monthlyTopUp
            }
            total
        } else {
            // Простой сложный процент без пополнения
            state.initialAmount * Math.pow(1 + monthlyRate, months.toDouble())
        }

        val totalTopUp = (monthlyTopUp ?: 0.0) * months
        val interestEarned = finalAmount - state.initialAmount - totalTopUp

        _resultState.update {
            ResultState(
                initialAmount = state.initialAmount,
                periodMonths = months,  // Используем выбранный срок
                interestRate = state.selectedRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                showResult = true
            )
        }
    }


    fun saveCalculation() {
        val result = _resultState.value
        if (result.showResult) {
            val calculation = DepositCalculation(
                initialAmount = result.initialAmount,
                periodMonths = result.periodMonths,
                interestRate = result.interestRate,
                monthlyTopUp = result.monthlyTopUp,
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

    // Сброс для нового расчёта
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