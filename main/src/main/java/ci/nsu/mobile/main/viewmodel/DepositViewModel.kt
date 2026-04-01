package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.local.TokenManager
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    // полностью удалили блок init { ... },
    // потому что Koin уже передал готовый к работе repository

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    // Берем логин из TokenManager и сразу получаем Flow с нужным списком
    val history = repository.getDepositsForUser(TokenManager.login ?: "unknown")

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
        viewModelScope.launch {
            repository.saveCalculation(
                DepositEntity(
                    // Берем логин из TokenManager (если null, то "unknown")
                    userLogin = TokenManager.login ?: "unknown",
                    initialAmount = state.initialAmount.toDoubleOrNull() ?: 0.0,
                    periodMonths = state.periodMonths.toIntOrNull() ?: 0,
                    interestRate = state.interestRate.toDoubleOrNull() ?: 0.0,
                    monthlyTopUp = state.monthlyTopUp.toDoubleOrNull() ?: 0.0,
                    finalAmount = state.finalAmount,
                    interestEarned = state.interestEarned
                )
            )
        }

        // Сброс формы для нового расчета
        fun reset() {
            _uiState.value = DepositUiState()
        }
    }
}