package ci.nsu.mobile.main.viewmodel.historyDeposits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.room.DepositCalculationEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDepositsViewModel @Inject constructor(
    val repository: DepositRepository): ViewModel()
{
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadHistory()
    }
    fun selectedDepositUpdate(deposit: DepositCalculationEntity) {
        _state.update { it.copy(selectedDeposit = deposit) }
    }

    fun loadHistory() {
        viewModelScope.launch {
                repository.getAll().collect { deposits -> _state.update { it.copy(deposits = deposits) }
            }
        }
    }
}