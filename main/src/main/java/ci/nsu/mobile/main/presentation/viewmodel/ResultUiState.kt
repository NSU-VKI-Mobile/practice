package ci.nsu.mobile.main.presentation.viewmodel

import ci.nsu.mobile.main.data.database.DepCalcs

sealed class ResultUiState {
    object Idle : ResultUiState()
    object Loading : ResultUiState()
    data class Success(val calculation: DepCalcs) : ResultUiState()
    data class Error(val message: String) : ResultUiState()
}