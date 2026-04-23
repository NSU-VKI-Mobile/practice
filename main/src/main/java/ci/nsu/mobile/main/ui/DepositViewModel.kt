package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DepositState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val interestRate: Double? = null,
    val monthlyTopUp: String = "",
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,
    val errorMessage: String? = null
)

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {
    // Можно менять (только внутри ViewModel)
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Main)
    // Только читать (для UI)
    val currentScreen: StateFlow<Screen> = _currentScreen

    private val _depositState = MutableStateFlow(DepositState())
    val depositState: StateFlow<DepositState> = _depositState

    private val _calculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val calculations: StateFlow<List<DepositCalculation>> = _calculations

    private val _selectedCalculation = MutableStateFlow<DepositCalculation?>(null)
    val selectedCalculation: StateFlow<DepositCalculation?> = _selectedCalculation

    private val availableRates = mapOf(
        "less6" to 15.0,   // до 6 месяцев
        "6to12" to 10.0,   // от 6 до 12 месяцев
        "more12" to 5.0    // от 12 месяцев
    )
    //Навигация между экранами
    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen  // Просто меняем значение потока
    }

    fun navigateBack() {
        when (_currentScreen.value) {
            Screen.Stage2 -> _currentScreen.value = Screen.Stage1
            Screen.Result -> _currentScreen.value = Screen.Stage2
            Screen.HistoryDetail -> _currentScreen.value = Screen.History
            else -> _currentScreen.value = Screen.Main
        }
    }

    fun navigateToMain() {
        _currentScreen.value = Screen.Main
        clearState()
    }

    fun updateInitialAmount(value: String) {
        _depositState.value = _depositState.value.copy(initialAmount = value)
    }

    fun updatePeriodMonths(value: String) {
        _depositState.value = _depositState.value.copy(periodMonths = value)
        updateAvailableRate()
    }  // При изменении срока пересчитываем ставку

    fun updateMonthlyTopUp(value: String) {
        _depositState.value = _depositState.value.copy(monthlyTopUp = value)
    }

    fun setInterestRate(rate: Double) {
        _depositState.value = _depositState.value.copy(interestRate = rate)
    }
//Логика выбора ставки
    private fun updateAvailableRate() {
        val period = _depositState.value.periodMonths.toIntOrNull()
        when {
            period == null -> {
                _depositState.value = _depositState.value.copy(
                    errorMessage = "Введите корректный срок"
                )
            }
            period < 6 -> {
                _depositState.value = _depositState.value.copy(
                    interestRate = availableRates["less6"],
                    errorMessage = null
                )
            }
            period < 12 -> {
                _depositState.value = _depositState.value.copy(
                    interestRate = availableRates["6to12"],
                    errorMessage = null
                )
            }
            else -> {
                _depositState.value = _depositState.value.copy(
                    interestRate = availableRates["more12"],
                    errorMessage = null
                )
            }
        }
    }
    //Валидация данных
    fun validateStage1(): Boolean {
        val state = _depositState.value
        return when {
            state.initialAmount.isBlank() -> {
                _depositState.value = state.copy(errorMessage = "Введите стартовый взнос")
                false
            }
            state.periodMonths.isBlank() -> {
                _depositState.value = state.copy(errorMessage = "Введите срок вклада")
                false
            }
            state.initialAmount.toDoubleOrNull() == null -> {
                _depositState.value = state.copy(errorMessage = "Введите корректную сумму")
                false
            }
            state.periodMonths.toIntOrNull() == null -> {
                _depositState.value = state.copy(errorMessage = "Введите корректный срок")
                false
            }
            else -> {
                _depositState.value = state.copy(errorMessage = null)
                true
            }
        }
    }

    fun validateStage2(): Boolean {
        val state = _depositState.value
        return when {
            state.interestRate == null -> {
                _depositState.value = state.copy(errorMessage = "Выберите процентную ставку")
                false
            }
            else -> {
                _depositState.value = state.copy(errorMessage = null)
                true
            }
        }
    }
    // Расчёт вклада
    fun calculateResult() {
        val state = _depositState.value
        val initial = state.initialAmount.toDouble()
        val period = state.periodMonths.toInt()
        val rate = state.interestRate ?: 0.0
        val monthly = state.monthlyTopUp.toDoubleOrNull() ?: 0.0

        var total = initial
        for (i in 1..period) {
            total += monthly
            total += total * rate / 100 / 12
        }

        val interestEarned = total - initial - (monthly * period)

        _depositState.value = state.copy(
            finalAmount = total,
            interestEarned = interestEarned
        )

        _currentScreen.value = Screen.Result
    }
    //Сохранение в базу данных
    fun saveCalculation() {
        val state = _depositState.value
        val calculation = DepositCalculation(
            initialAmount = state.initialAmount.toDouble(),
            periodMonths = state.periodMonths.toInt(),
            interestRate = state.interestRate ?: 0.0,
            monthlyTopUp = state.monthlyTopUp.toDoubleOrNull(),
            finalAmount = state.finalAmount,
            interestEarned = state.interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertCalculation(calculation)  // Асинхронное сохранение
        }
    }

    fun loadAllCalculations() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _calculations.value = list // Автообновление при изменениях
            }
        }
    }

    fun loadCalculationById(id: Long) {
        viewModelScope.launch {
            _selectedCalculation.value = repository.getCalculationById(id)
        }
    }

    private fun clearState() {
        _depositState.value = DepositState()
    }
}