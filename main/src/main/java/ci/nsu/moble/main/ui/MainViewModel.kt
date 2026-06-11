// Task_5: ViewModel — навигация + состояние + бизнес-логика.
// Хранит uiState для каждого экрана, управляет переходами через sealed class Screen.

package ci.nsu.moble.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.AppDatabase
import ci.nsu.moble.main.data.DepositCalculation
import ci.nsu.moble.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
    data object Main : Screen()
    data object Stage1 : Screen()
    data object Stage2 : Screen()
    data object Result : Screen()
    data object History : Screen()
    data class Detail(val calculationId: Long) : Screen()
}

data class Stage1State(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val error: String? = null
)

data class Stage2State(
    val selectedRateIndex: Int = 0,
    val availableRates: List<Double> = emptyList(),
    val monthlyTopUp: String = "",
    val error: String? = null
)

data class ResultState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null,
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,
    val saved: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository by lazy {
        DepositRepository(AppDatabase.getDatabase(application).depositDao())
    }

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Main)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _stage1 = MutableStateFlow(Stage1State())
    val stage1: StateFlow<Stage1State> = _stage1.asStateFlow()

    private val _stage2 = MutableStateFlow(Stage2State())
    val stage2: StateFlow<Stage2State> = _stage2.asStateFlow()

    private val _result = MutableStateFlow(ResultState())
    val result: StateFlow<ResultState> = _result.asStateFlow()

    private val _calculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val calculations: StateFlow<List<DepositCalculation>> = _calculations.asStateFlow()

    private val _selectedCalculation = MutableStateFlow<DepositCalculation?>(null)
    val selectedCalculation: StateFlow<DepositCalculation?> = _selectedCalculation.asStateFlow()

    init {
        loadHistory()
    }

    // ---- Navigation ----

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // ---- Stage 1 ----

    fun onInitialAmountChanged(value: String) {
        _stage1.update { it.copy(initialAmount = value, error = null) }
    }

    fun onPeriodMonthsChanged(value: String) {
        _stage1.update { it.copy(periodMonths = value, error = null) }
    }

    fun onStage1Next() {
        val state = _stage1.value
        val amount = state.initialAmount.toDoubleOrNull()
        val period = state.periodMonths.toIntOrNull()

        if (amount == null || amount <= 0) {
            _stage1.update { it.copy(error = "Введите корректный стартовый взнос") }
            return
        }
        if (period == null || period <= 0) {
            _stage1.update { it.copy(error = "Введите корректный срок вклада") }
            return
        }

        val rates = buildList {
            if (period < 6) add(5.0)
            if (period >= 6 && period < 12) add(10.0)
            if (period >= 12) add(15.0)
        }

        _stage2.value = Stage2State(
            selectedRateIndex = 0,
            availableRates = rates
        )
        _currentScreen.value = Screen.Stage2
    }

    // ---- Stage 2 ----

    fun onRateSelected(index: Int) {
        _stage2.update { it.copy(selectedRateIndex = index) }
    }

    fun onMonthlyTopUpChanged(value: String) {
        _stage2.update { it.copy(monthlyTopUp = value) }
    }

    fun onStage2Calculate() {
        val s1 = _stage1.value
        val s2 = _stage2.value
        val amount = s1.initialAmount.toDoubleOrNull() ?: return
        val period = s1.periodMonths.toIntOrNull() ?: return

        if (s2.availableRates.isEmpty()) {
            _stage2.update { it.copy(error = "Срок не указан — введите корректный срок") }
            return
        }

        val rate = s2.availableRates[s2.selectedRateIndex]
        val topUp = s2.monthlyTopUp.toDoubleOrNull()?.takeIf { it > 0 }

        // Простой расчёт: проценты = сумма * ставка% * (срок / 12)
        val interestEarned = amount * (rate / 100.0) * (period / 12.0)

        // Если есть ежемесячное пополнение: считаем как средний срок для пополнений
        val topUpTotal = topUp?.let { it * period } ?: 0.0
        val topUpInterest = topUp?.let {
            it * (rate / 100.0) * (period / 12.0 / 2.0) * period
        } ?: 0.0

        val totalInterest = interestEarned + topUpInterest
        val finalAmount = amount + topUpTotal + totalInterest

        _result.value = ResultState(
            initialAmount = amount,
            periodMonths = period,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = finalAmount,
            interestEarned = totalInterest,
            saved = false
        )
        _currentScreen.value = Screen.Result
    }

    // ---- Save ----

    fun saveResult() {
        val res = _result.value
        viewModelScope.launch {
            repository.saveCalculation(
                DepositCalculation(
                    initialAmount = res.initialAmount,
                    periodMonths = res.periodMonths,
                    interestRate = res.interestRate,
                    monthlyTopUp = res.monthlyTopUp,
                    finalAmount = res.finalAmount,
                    interestEarned = res.interestEarned,
                    calculationDate = System.currentTimeMillis()
                )
            )
            _result.update { it.copy(saved = true) }
            loadHistory()
        }
    }

    // ---- History ----

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _calculations.value = list
            }
        }
    }

    fun loadCalculationDetail(id: Long) {
        viewModelScope.launch {
            _selectedCalculation.value = repository.getCalculation(id)
        }
    }
    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            repository.deleteCalculation(id)
            // Обновить список расчетов
            loadHistory()
        }
    }
}
