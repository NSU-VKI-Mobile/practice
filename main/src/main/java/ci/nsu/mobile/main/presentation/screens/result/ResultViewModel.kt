package ci.nsu.mobile.main.presentation.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.domain.model.DepositCalculation
import ci.nsu.mobile.main.domain.repository.DepositRepository
import ci.nsu.mobile.main.domain.usecase.CalculateDepositUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: DepositRepository,
    private val calculateDepositUseCase: CalculateDepositUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    fun calculateDeposit(
        initialAmount: String,
        periodMonths: String,
        interestRate: Double,
        monthlyTopUp: String?
    ) {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val topUp = monthlyTopUp?.toDoubleOrNull()

        val calculation = calculateDepositUseCase(
            initialAmount = amount,
            periodMonths = months,
            interestRate = interestRate,
            monthlyTopUp = topUp
        )

        _uiState.value = _uiState.value.copy(calculation = calculation, isSaved = false)
    }

    fun saveCalculation() {
        viewModelScope.launch {
            _uiState.value.calculation?.let { calculation ->
                val userId = getUserIdFromToken()
                repository.saveCalculation(userId, calculation)
                _uiState.value = _uiState.value.copy(isSaved = true)
            }
        }
    }

    private fun getUserIdFromToken(): Long {
        return try {
            val token = tokenManager.token ?: return 0L
            val payload = token.split(".")[1]
            val decoded = android.util.Base64.decode(payload, android.util.Base64.DEFAULT)
            val json = String(decoded)
            val idField = "\"id\":"
            val start = json.indexOf(idField) + idField.length
            val end = json.indexOf(",", start)
            json.substring(start, end).trim().toLong()
        } catch (_: Exception) {
            0L
        }
    }
}

data class ResultUiState(
    val calculation: DepositCalculation? = null,
    val isSaved: Boolean = false
)