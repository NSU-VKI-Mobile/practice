package ci.nsu.mobile.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// UiState для главного экрана
data class MainUiState(
    val currentScreen: Screen = Screen.Home
)

// UiState для экрана ввода параметров
data class InputUiState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val monthlyTopUp: String = "",
    val error: String? = null,
    val isValid: Boolean = false
)

// UiState для экрана результата
data class ResultUiState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null,
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,
    val calculationDate: String = ""
)

// Экраны приложения
sealed class Screen {
    object Home : Screen()
    object Step1 : Screen()
    object Step2 : Screen()
    object Result : Screen()
    object History : Screen()
    object Detail : Screen()
}

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository

    init {
        val database = AppDatabase.getDatabase(application)
        val dao = database.depositDao()
        repository = DepositRepository(dao)
    }

    // Состояние навигации
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    // Состояние ввода данных
    private val _inputState = MutableStateFlow(InputUiState())
    val inputState: StateFlow<InputUiState> = _inputState.asStateFlow()

    // Состояние результата
    private val _resultState = MutableStateFlow(ResultUiState())
    val resultState: StateFlow<ResultUiState> = _resultState.asStateFlow()

    // Список истории
    val historyList = repository.allDeposits

    // Навигация
    fun navigateTo(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    // Обновление полей ввода
    fun updateInitialAmount(value: String) {
        _inputState.value = _inputState.value.copy(initialAmount = value)
        validateInput()
    }

    fun updatePeriodMonths(value: String) {
        _inputState.value = _inputState.value.copy(periodMonths = value)
        validateInput()
    }

    fun updateMonthlyTopUp(value: String) {
        _inputState.value = _inputState.value.copy(monthlyTopUp = value)
        validateInput()
    }

    // Валидация ввода
    private fun validateInput() {
        val state = _inputState.value
        val initial = state.initialAmount.toDoubleOrNull()
        val months = state.periodMonths.toIntOrNull()

        val isValid = initial != null && initial > 0 && months != null && months > 0

        _inputState.value = state.copy(
            isValid = isValid,
            error = if (!isValid && (initial == null || months == null))
                "Заполните обязательные поля корректно" else null
        )
    }

    // Расчёт процентной ставки
    fun getInterestRate(months: Int): Double {
        return when {
            months < 6 -> 15.0
            months < 12 -> 10.0
            else -> 5.0
        }
    }

    // Расчёт итоговой суммы
    fun calculateResult() {
        val state = _inputState.value
        val initial = state.initialAmount.toDoubleOrNull() ?: 0.0
        val months = state.periodMonths.toIntOrNull() ?: 0
        val topUp = state.monthlyTopUp.toDoubleOrNull()

        if (initial <= 0 || months <= 0) {
            _inputState.value = state.copy(error = "Некорректные данные")
            return
        }

        val rate = getInterestRate(months) / 100 / 12 // месячная ставка
        var amount = initial
        var totalInterest = 0.0

        for (i in 1..months) {
            val interest = amount * rate
            totalInterest += interest
            amount += interest
            if (topUp != null && topUp > 0) {
                amount += topUp
            }
        }

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateStr = dateFormat.format(Date())

        _resultState.value = ResultUiState(
            initialAmount = initial,
            periodMonths = months,
            interestRate = rate * 12 * 100, // годовая ставка
            monthlyTopUp = topUp,
            finalAmount = amount,
            interestEarned = totalInterest,
            calculationDate = dateStr
        )

        navigateTo(Screen.Result)
    }

    // Сохранение в базу
    fun saveCalculation() {
        viewModelScope.launch {
            val result = _resultState.value
            val deposit = DepositCalculation(
                initialAmount = result.initialAmount,
                periodMonths = result.periodMonths,
                interestRate = result.interestRate,
                monthlyTopUp = result.monthlyTopUp,
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned,
                calculationDate = System.currentTimeMillis()
            )
            repository.insert(deposit)
            navigateTo(Screen.History)
        }
    }

    // Закрытие приложения
    fun closeApp() {
        // В реальном приложении здесь был бы вызов finish()
        // Для Compose можно использовать LocalContext.current.finish()
    }
}