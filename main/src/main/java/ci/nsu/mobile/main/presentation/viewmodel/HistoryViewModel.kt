package ci.nsu.mobile.main.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.DepositApp
import ci.nsu.mobile.main.data.database.DepCalcs
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as DepositApp).repository

    val uiState: StateFlow<HistoryUiState> = repository.getAllCalculations()
        .map { list -> HistoryUiState.Success(items = list) as HistoryUiState }
        .catch { e ->
            emit(HistoryUiState.Error(e.message ?: "Ошибка загрузки"))
        }
        .stateIn(viewModelScope, WhileSubscribed(5000), HistoryUiState.Loading)
}

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val items: List<DepCalcs>) : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}