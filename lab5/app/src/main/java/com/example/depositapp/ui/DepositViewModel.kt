package com.example.depositapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.depositapp.data.db.DepositCalculation
import com.example.depositapp.data.repository.DepositRepository
import com.example.depositapp.domain.DepositInput
import com.example.depositapp.domain.calculateDeposit
import com.example.depositapp.domain.getRateForPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UiState — состояние всего приложения
// Один ViewModel на все экраны — данные не теряются при навигации
data class DepositUiState(
    // Данные вводимые пользователем
    val initialAmountText: String = "",
    val periodMonthsText: String = "",
    val monthlyTopUpText: String = "",
    val selectedRate: Double = 0.0,

    // Результат расчёта
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,

    // Флаги состояния
    val isSaved: Boolean = false,
    val saveError: String? = null
)

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    // История расчётов — живой поток из базы данных
    // Flow<List<...>> — автоматически обновляется при изменениях в базе
    val history: Flow<List<DepositCalculation>> = repository.getAll()

    // --- Обработчики полей ввода ---

    fun onInitialAmountChanged(value: String) {
        _uiState.update { it.copy(initialAmountText = value) }
    }

    fun onPeriodChanged(value: String) {
        // При смене срока — пересчитываем ставку
        val months = value.toIntOrNull() ?: 0
        val rate = if (months > 0) getRateForPeriod(months) else 0.0
        _uiState.update { it.copy(periodMonthsText = value, selectedRate = rate) }
    }

    fun onMonthlyTopUpChanged(value: String) {
        _uiState.update { it.copy(monthlyTopUpText = value) }
    }

    // --- Бизнес-логика ---

    // Вычислить результат (вызывается перед экраном результата)
    fun calculate() {
        val state = _uiState.value
        val input = DepositInput(
            initialAmount = state.initialAmountText.toDoubleOrNull() ?: 0.0,
            periodMonths = state.periodMonthsText.toIntOrNull() ?: 0,
            interestRate = state.selectedRate,
            monthlyTopUp = state.monthlyTopUpText.toDoubleOrNull() ?: 0.0
        )
        val (final, interest) = calculateDeposit(input)
        _uiState.update { it.copy(finalAmount = final, interestEarned = interest, isSaved = false) }
    }

    // Сохранить в базу данных
    // viewModelScope — корутина живёт пока жив ViewModel
    // launch — запускаем асинхронную операцию
    fun save() {
        val state = _uiState.value
        viewModelScope.launch {
            try {
                repository.save(
                    DepositCalculation(
                        initialAmount = state.initialAmountText.toDoubleOrNull() ?: 0.0,
                        periodMonths = state.periodMonthsText.toIntOrNull() ?: 0,
                        interestRate = state.selectedRate,
                        monthlyTopUp = state.monthlyTopUpText.toDoubleOrNull(),
                        finalAmount = state.finalAmount,
                        interestEarned = state.interestEarned,
                        calculationDate = System.currentTimeMillis() // текущее время
                    )
                )
                _uiState.update { it.copy(isSaved = true, saveError = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(saveError = "Ошибка сохранения: ${e.message}") }
            }
        }
    }

    // Сброс для нового расчёта
    fun reset() {
        _uiState.update { DepositUiState() }
    }

    // Валидация шага 1
    fun isStep1Valid(): Boolean {
        val amount = _uiState.value.initialAmountText.toDoubleOrNull()
        val months = _uiState.value.periodMonthsText.toIntOrNull()
        return amount != null && amount > 0 && months != null && months > 0
    }

    // --- ViewModelFactory ---
    // Фабрика нужна потому что наш ViewModel принимает параметры (repository)
    // Стандартный viewModel() не умеет передавать параметры
    class Factory(private val repository: DepositRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Проверяем что запрашивают именно DepositViewModel
            if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DepositViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
