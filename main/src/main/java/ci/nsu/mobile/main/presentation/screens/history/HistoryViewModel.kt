package ci.nsu.mobile.main.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.domain.model.DepositCalculation
import ci.nsu.mobile.main.domain.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: DepositRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init { loadHistory() }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getCalculationsForUser(getUserIdFromToken()).collect { calculations ->
                _uiState.value = _uiState.value.copy(calculations = calculations, isLoading = false)
            }
        }
    }

    fun selectCalculation(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedCalculation = repository.getCalculationById(id))
        }
    }

    private fun getUserIdFromToken(): Long {
        return try {
            val token = tokenManager.token ?: return 0L
            val payload = token.split(".")[1]
            val decoded = android.util.Base64.decode(payload, android.util.Base64.DEFAULT)
            val json = String(decoded)
            json.substring(json.indexOf("\"id\":") + 5, json.indexOf(",", json.indexOf("\"id\":"))).trim().toLong()
        } catch (_: Exception) { 0L }
    }
}

data class HistoryUiState(
    val calculations: List<DepositCalculation> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCalculation: DepositCalculation? = null
)