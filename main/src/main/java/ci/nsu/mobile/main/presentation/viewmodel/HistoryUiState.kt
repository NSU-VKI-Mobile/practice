package ci.nsu.mobile.main.presentation.viewmodel

import ci.nsu.mobile.main.data.database.DepCalcs

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val items: List<DepCalcs>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}