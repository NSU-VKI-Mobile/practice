package ci.nsu.mobile.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ci.nsu.mobile.main.data.database.AppDatabase

class HistoryViewModel(
    private val database: AppDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            try {
                // Собираем Flow в список
                val items = mutableListOf<ci.nsu.mobile.main.data.database.DepCalcs>()
                database.depositDao().getAll().collect { list ->
                    items.clear()
                    items.addAll(list)
                    _uiState.value = HistoryUiState.Success(items)
                }
            } catch (e: Exception) {
                _uiState.value = HistoryUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }
}