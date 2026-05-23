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

    fun selectedDepositUpdate(deposit: DepositCalculationEntity) {
        _state.update { it.copy(selectedDeposit = deposit) }
    }

    fun deleteDeposit(deposit: DepositCalculationEntity) {
        viewModelScope.launch {
            repository.deleteDeposit(deposit)
        }
    }
    fun loadHistory() {
        viewModelScope.launch {
            val userId = tokenManager.userId.toLong()
            android.util.Log.d("HISTORY_DEBUG", "Current userId: $userId")

            repository.getAll(userId).collect { deposits ->
                android.util.Log.d("HISTORY_DEBUG", "Total deposits: ${deposits.size}")
                deposits.forEach { deposit ->
                    android.util.Log.d("HISTORY_DEBUG", "Deposit id=${deposit.id}, userId=${deposit.userId}")
                }
                _state.update { it.copy(deposits = deposits) }
            }
        }
    }
}