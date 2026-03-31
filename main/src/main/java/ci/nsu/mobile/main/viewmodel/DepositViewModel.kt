package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositEntity
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository

    init {
        // Инициализируем базу данных и репозиторий при создании ViewModel
        val dao = AppDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(dao)
    }

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    // Публикуем историю из БД напрямую для UI
    val history = repository.allDeposits

    // Обновление состояния при вводе текста
    fun updateState(newState: DepositUiState) {
        _uiState.value = newState
    }

    // Логика доступных ставок по заданию
    fun getAvailableRates(): List<String> {
        val months = _uiState.value.periodMonths.toIntOrNull() ?: return emptyList()
        return when {
            months < 6 -> listOf("15")
            months in 6..11 -> listOf("10")
            else -> listOf("5")
        }
    }

    // Расчет вклада с учетом ежемесячной капитализации и пополнений
    fun calculate() {
        val state = _uiState.value
        val initial = state.initialAmount.toDoubleOrNull() ?: 0.0
        val months = state.periodMonths.toIntOrNull() ?: 0
        val rate = state.interestRate.toDoubleOrNull() ?: 0.0
        val topUp = state.monthlyTopUp.toDoubleOrNull() ?: 0.0

        var currentSum = initial
        var totalInterest = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            currentSum += topUp
            val interestForMonth = currentSum * monthlyRate
            totalInterest += interestForMonth
            currentSum += interestForMonth
        }

        _uiState.update {
            it.copy(
                finalAmount = currentSum,
                interestEarned = totalInterest
            )
        }
    }

    // Сохранение в базу данных
    fun saveCalculation() {
        val state = _uiState.value
        // Запускаем корутину, так как запись в БД нельзя делать в главном потоке
        viewModelScope.launch {
            repository.saveCalculation(
                DepositEntity(
                    initialAmount = state.initialAmount.toDoubleOrNull() ?: 0.0,
                    periodMonths = state.periodMonths.toIntOrNull() ?: 0,
                    interestRate = state.interestRate.toDoubleOrNull() ?: 0.0,
                    monthlyTopUp = state.monthlyTopUp.toDoubleOrNull() ?: 0.0,
                    finalAmount = state.finalAmount,
                    interestEarned = state.interestEarned
                )
            )
        }
    }

    // Сброс формы для нового расчета
    fun reset() {
        _uiState.value = DepositUiState()
    }
}