package ci.nsu.mobile.main.viewmodel.historyDeposits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.room.DepositCalculationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDepositsViewModel @Inject constructor(
    val repository: DepositRepository,
    val tokenManager: TokenManager): ViewModel()
{
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    fun historyEvent(event: HistoryEvents) {
        when(event) {
            is HistoryEvents.DeleteDeposit -> deleteDeposit(event.deposit)
            is HistoryEvents.LoadHistory -> loadHistory()
            is HistoryEvents.SelectedDepositUpdate -> {
                _state.update { it.copy(selectedDeposit = event.newDeposit) }
            }
        }
    }

    private fun deleteDeposit(deposit: DepositCalculationEntity) {
        viewModelScope.launch {
            repository.deleteDeposit(deposit)
        }
    }
    private fun loadHistory() {
        viewModelScope.launch {
            val userId = tokenManager.userId.toLong()
            repository.getAll(userId).collect { deposits ->
                _state.update { it.copy(deposits = deposits) }
            }
        }
    }
}