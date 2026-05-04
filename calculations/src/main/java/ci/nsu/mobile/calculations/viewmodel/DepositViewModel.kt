package ci.nsu.mobile.calculations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.calculations.domain.DepositCalculator
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    fun getHistory(userId: Long): Flow<List<DepositCalculation>> {
        return repository.getCalculationsForUser(userId)
    }

    fun updateState(newState: DepositUiState) {
        _uiState.value = newState
    }

    fun getAvailableRates(): List<String> {
        val months = _uiState.value.periodMonths.toIntOrNull() ?: return emptyList()
        return DepositCalculator.availableRates(months)
    }

    fun calculate() {
        val state = _uiState.value
        val result = DepositCalculator.calculate(
            initialAmount = state.initialAmount.toDoubleOrNull() ?: 0.0,
            periodMonths = state.periodMonths.toIntOrNull() ?: 0,
            interestRate = state.interestRate.toDoubleOrNull() ?: 0.0,
            monthlyTopUp = state.monthlyTopUp.toDoubleOrNull() ?: 0.0
        )

        _uiState.update {
            it.copy(
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned
            )
        }
    }

    fun saveCalculation(userId: Long) {
        val state = _uiState.value
        viewModelScope.launch {
            repository.saveCalculation(
                DepositCalculation(
                    userId = userId,
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

    fun reset() {
        _uiState.value = DepositUiState()
    }
}
