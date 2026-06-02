package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.models.DepositCalculation
import ci.nsu.moble.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DepositUiState {
    object Loading : DepositUiState()
    data class Success(val calculations: List<DepositCalculation>) : DepositUiState()
    data class Error(val message: String) : DepositUiState()
}

class DepositViewModel(
    private val repository: DepositRepository,
    private val userId: Long
) : ViewModel() {

    private val _state = MutableStateFlow<DepositUiState>(DepositUiState.Loading)
    val state: StateFlow<DepositUiState> = _state.asStateFlow()

    init {
        loadCalculations()
    }

    fun loadCalculations() {
        viewModelScope.launch {
            _state.value = DepositUiState.Loading
            repository.getCalculationsForUser(userId).collect { calculations ->
                _state.value = DepositUiState.Success(calculations)
            }
        }
    }

    fun saveCalculation(initialAmount: Double, periodMonths: Int, interestRate: Double, monthlyTopUp: Double?) {
        viewModelScope.launch {
            val monthlyRate = interestRate / 100 / 12
            val finalAmount = if (monthlyTopUp != null && monthlyTopUp > 0) {
                var total = initialAmount
                repeat(periodMonths) {
                    total = total * (1 + monthlyRate) + monthlyTopUp
                }
                total
            } else {
                initialAmount * Math.pow(1 + monthlyRate, periodMonths.toDouble())
            }
            val totalTopUp = (monthlyTopUp ?: 0.0) * periodMonths
            val interestEarned = finalAmount - initialAmount - totalTopUp

            val calculation = DepositCalculation(
                userId = userId,
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned
            )
            repository.saveCalculation(calculation)
            loadCalculations()
        }
    }

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
            loadCalculations()
        }
    }
}