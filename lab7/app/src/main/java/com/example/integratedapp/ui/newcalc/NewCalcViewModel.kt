package com.example.integratedapp.ui.newcalc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integratedapp.data.SessionManager
import com.example.integratedapp.data.db.DepositCalculation
import com.example.integratedapp.data.repository.DepositRepository
import com.example.integratedapp.domain.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Этапы создания нового расчёта
enum class CalcStep { STEP1, STEP2, RESULT }

data class NewCalcUiState(
    val currentStep: CalcStep = CalcStep.STEP1,

    // Ввод
    val initialAmountText: String = "",
    val periodMonthsText: String = "",
    val monthlyTopUpText: String = "",
    val selectedRate: Double = 0.0,

    // Результат
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,

    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

class NewCalcViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewCalcUiState())
    val uiState: StateFlow<NewCalcUiState> = _uiState.asStateFlow()

    fun onInitialAmountChanged(v: String) {
        _uiState.update { it.copy(initialAmountText = v) }
    }

    fun onPeriodChanged(v: String) {
        val months = v.toIntOrNull() ?: 0
        val rate = if (months > 0) getRateForPeriod(months) else 0.0
        _uiState.update { it.copy(periodMonthsText = v, selectedRate = rate) }
    }

    fun onMonthlyTopUpChanged(v: String) {
        _uiState.update { it.copy(monthlyTopUpText = v) }
    }

    // Переход к следующему шагу
    fun goToStep2(): Boolean {
        val amount = _uiState.value.initialAmountText.toDoubleOrNull()
        val months = _uiState.value.periodMonthsText.toIntOrNull()
        if (amount == null || amount <= 0 || months == null || months <= 0) {
            _uiState.update { it.copy(errorMessage = "Введите корректные данные") }
            return false
        }
        _uiState.update { it.copy(currentStep = CalcStep.STEP2, errorMessage = null) }
        return true
    }

    fun goBackToStep1() {
        _uiState.update { it.copy(currentStep = CalcStep.STEP1) }
    }

    fun goBackToStep2() {
        _uiState.update { it.copy(currentStep = CalcStep.STEP2, isSaved = false) }
    }

    // Рассчитать и перейти к результату
    fun calculate() {
        val state = _uiState.value
        val input = DepositInput(
            initialAmount = state.initialAmountText.toDoubleOrNull() ?: 0.0,
            periodMonths = state.periodMonthsText.toIntOrNull() ?: 0,
            interestRate = state.selectedRate,
            monthlyTopUp = state.monthlyTopUpText.toDoubleOrNull() ?: 0.0
        )
        val (final, interest) = calculateDeposit(input)
        _uiState.update {
            it.copy(
                currentStep = CalcStep.RESULT,
                finalAmount = final,
                interestEarned = interest,
                isSaved = false
            )
        }
    }

    // Сохранить с привязкой к пользователю
    fun save() {
        val state = _uiState.value
        viewModelScope.launch {
            try {
                repository.save(
                    DepositCalculation(
                        userId = SessionManager.userId, // <-- привязка к пользователю
                        initialAmount = state.initialAmountText.toDoubleOrNull() ?: 0.0,
                        periodMonths = state.periodMonthsText.toIntOrNull() ?: 0,
                        interestRate = state.selectedRate,
                        monthlyTopUp = state.monthlyTopUpText.toDoubleOrNull(),
                        finalAmount = state.finalAmount,
                        interestEarned = state.interestEarned,
                        calculationDate = System.currentTimeMillis()
                    )
                )
                _uiState.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Ошибка сохранения") }
            }
        }
    }

    // Начать новый расчёт
    fun reset() {
        _uiState.update { NewCalcUiState() }
    }
}
