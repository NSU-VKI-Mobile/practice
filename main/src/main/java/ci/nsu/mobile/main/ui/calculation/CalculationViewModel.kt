package ci.nsu.mobile.main.ui.calculation

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.ui.main.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculationViewModel constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(CalculationUiState())
    val uiState: StateFlow<CalculationUiState> = _uiState.asStateFlow()

    // Логика ViewModel

    fun updateDepositAmount(amount: String) {
        _uiState.update { currentState ->
            val isValid = amount.isEmpty() || amount.toDoubleOrNull() != null
            currentState.copy(
                depositAmount = amount,
                isAmountValid = isValid,
                canProceed = isValid && currentState.isTermValid &&
                        amount.isNotBlank() && currentState.depositTerm.isNotBlank(),

            )
        }
    }

    fun updateDepositTerm(period: String) {
        _uiState.update { currentState ->
            val isValid = period.isEmpty() || period.toIntOrNull() != null
            currentState.copy(
                depositTerm = period,
                isTermValid = isValid,
                canProceed = currentState.isAmountValid && isValid &&
                        currentState.depositAmount.isNotBlank() && period.isNotBlank()
            )
        }
    }
}