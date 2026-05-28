package ci.nsu.mobile.main.calculations.ui.calculation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CalculationUiState())
    val uiState: StateFlow<CalculationUiState> = _uiState.asStateFlow()

    fun updateDepositAmount(amount: String) {
        _uiState.update { currentState ->
            val isValid = amount.isEmpty() || amount.toDoubleOrNull() != null
            currentState.copy(
                depositAmount = amount,
                isAmountValid = isValid,
                canProceed = isValid && currentState.isTermValid &&
                        amount.isNotBlank() && currentState.depositTerm.isNotBlank()
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