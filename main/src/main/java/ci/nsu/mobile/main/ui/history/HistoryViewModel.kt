// ui/history/HistoryViewModel.kt
package ci.nsu.mobile.ui.history

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculationEntity
import ci.nsu.mobile.main.ui.history.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val context: Context
) : ViewModel() {

    private val database = AppDatabase.getDatabase(context)
    private val dao = database.depositDao()

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllCalculations().collect { calculations ->
                _uiState.update { it.copy(calculations = calculations) }
            }
        }
    }

    fun selectCalculation(calculation: DepositCalculationEntity) {
        _uiState.update { it.copy(selectedCalculation = calculation) }
    }

    fun clearSelectedCalculation() {
        _uiState.update { it.copy(selectedCalculation = null) }
    }
}