package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.model.DepositUIState
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DepositCalculationViewModel(repos: DepositRepository): ViewModel() {
    private val _uiState = MutableStateFlow(DepositUIState())
    val uiState: StateFlow<DepositUIState> = _uiState.asStateFlow()

    fun validationFirstScreen(): String {
        val state = _uiState.value
        if (state.initialAmount.isEmpty()) {
            return "Введите стартовый взнос!"
        }
        if (state.initialAmount.toDoubleOrNull() == null) {
            return "Стартовый взнос должен быть числом"
        }
        if (state.periodMonths.isEmpty()) {
            return "Введите срок вклада в месяцах"
        }
        if (state.periodMonths.toDoubleOrNull() == null) {
            return "Срок вклада должен быть целым числом"
        }
        return ""
    }

    fun validationSecondScreen(isChecked: Boolean): String {
        val state = _uiState.value
        if (isChecked) {
            if (state.interestRate.isEmpty() || state.interestRate.toDoubleOrNull() == null) {
                return "Укажите процентную ставку"
            }
            if (state.monthlyTopUp?.toIntOrNull() == null) {
                return "Укажите сумму ежемесячного пополнения"
            }
        }
        else {
            if (state.interestRate.isEmpty() || state.interestRate.toDoubleOrNull() == null) {
                return "Укажите процентную ставку"
            }
        }
        return ""
    }
    fun initialAmountUpdate(newValue: String) {
        _uiState.update { it.copy(initialAmount = newValue) }
    }

    fun periodMonthUpdate(newValue: String) {
        _uiState.update { it.copy(periodMonths = newValue) }
    }

    fun monthlyTopUpUpdate(newValue: String) {
        _uiState.update { it.copy(monthlyTopUp = newValue) }
    }
    fun interestRateUpdate(newValue: String) {
        _uiState.update { it.copy(interestRate = newValue) }
    }

    fun calculateFinalAmount(initialAmount: Double, interestRate: Int, period: Int, monthlyTopUp: Double?) {
        _uiState.update { it.copy(interestEarned = (initialAmount*interestRate*period*30.14)/(365*100)) }
        _uiState.update { it.copy(finalAmount = (initialAmount + (initialAmount*interestRate*period*30.14)/(365*100)))}
    }

    fun calculateDate(date: Long) {
        _uiState.update { it.copy( calculationDate = date) }
    }
    fun cleanAll() {
        _uiState.update { DepositUIState() }
    }
}
