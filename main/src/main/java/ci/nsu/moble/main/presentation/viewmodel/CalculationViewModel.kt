package ci.nsu.moble.main.presentation.viewmodel

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
import ci.nsu.moble.main.domain.DepositUiState

class CalculationViewModel(application: Application) : AndroidViewModel(application) {

    // База данных и репозиторий
    private val db = AppDatabase.getDatabase(application)
    private val repo = DepositRepository(db.depositDao())

    // Состояние экрана (храним все данные тут)
    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    // 1. Обновление стартового взноса
    fun onInitialAmountChanged(value: String) {
        _uiState.update { it.copy(initialAmount = value) }
        // Сразу обновляем ставку, если срок уже введен
        updateInterestRate()
    }

    // 2. Обновление срока
    fun onPeriodMonthsChanged(value: String) {
        _uiState.update { it.copy(periodMonths = value) }
        // Сразу обновляем ставку
        updateInterestRate()
    }

    // 3. Обновление пополнения
    fun onMonthlyTopUpChanged(value: String) {
        _uiState.update { it.copy(monthlyTopUp = value) }
    }

    // 4. Логика определения ставки (самое важное!)
    private fun updateInterestRate() {
        val months = _uiState.value.periodMonths.toIntOrNull()

        val rate = when {
            months == null -> 0.0 // Если ничего не введено
            months in 1..5 -> 0.15 // 15%
            months in 6..11 -> 0.10 // 10%
            months >= 12 -> 0.05 // 5%
            else -> 0.0
        }

        // Обновляем ставку в состоянии
        _uiState.update { it.copy(interestRate = rate) }
    }

    // 5. Расчет итоговой суммы
    fun calculate() {
        val initial = _uiState.value.initialAmount.toDoubleOrNull() ?: 0.0
        val months = _uiState.value.periodMonths.toIntOrNull() ?: 0
        val rate = _uiState.value.interestRate
        val topUp = _uiState.value.monthlyTopUp.toDoubleOrNull() ?: 0.0

        // Простая формула для примера (можно усложнить)
        val interest = (initial * rate * months) + (topUp * rate * months)
        val finalAmount = initial + interest + (topUp * months)

        _uiState.update {
            it.copy(
                interestEarned = interest,
                finalAmount = finalAmount,
                // Сохраняем дату
                calculationDate = System.currentTimeMillis()
            )
        }
    }

    // 6. Сохранение в Базу Данных (Room)
    fun saveToDatabase() {
        val state = _uiState.value
        val entity = DepositCalculation(
            initialAmount = state.initialAmount.toDoubleOrNull() ?: 0.0,
            periodMonths = state.periodMonths.toIntOrNull() ?: 0,
            interestRate = state.interestRate,
            monthlyTopUp = state.monthlyTopUp.toDoubleOrNull() ?: 0.0,
            interestEarned = state.interestEarned,
            finalAmount = state.finalAmount,
            calculationDate = state.calculationDate
        )

        viewModelScope.launch {
            repo.saveCalculation(entity)
        }
    }

    // Сброс для нового расчёта (опционально)
    fun reset() {
        _uiState.value = DepositUiState()
    }
}