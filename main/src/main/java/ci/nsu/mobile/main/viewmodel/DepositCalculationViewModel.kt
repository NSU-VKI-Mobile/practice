package ci.nsu.mobile.main.viewmodel

import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    fun validationFirstScreen(): Boolean {
        val state = _uiState.value
        if (state.initialAmount.isEmpty()) {
            return false
        }
        if (state.initialAmount.toDoubleOrNull() == null) {
            return false
        }
        if (state.periodMonths.isEmpty()) {
            return false
        }
        if (state.periodMonths.toDoubleOrNull() == null) {
            return false
        }
        return true
    }

    fun initialAmountUpdate(newValue: String) {
        _uiState.update { it.copy(initialAmount = newValue) }
    }

    fun periodMonthUpdate(newValue: String) {
        _uiState.update { it.copy(periodMonths = newValue) }
    }

    fun cleanAll() {
        _uiState.update { DepositUIState() }
    }
}
