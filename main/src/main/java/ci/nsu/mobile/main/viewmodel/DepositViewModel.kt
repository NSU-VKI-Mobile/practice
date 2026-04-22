package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _history = MutableStateFlow<List<DepositEntity>>(emptyList())
    val history: StateFlow<List<DepositEntity>> = _history

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getHistory()
        }
    }

    fun insertDeposit(deposit: DepositEntity) {
        viewModelScope.launch {
            repository.insertDeposit(deposit)
            loadHistory()
        }
    }
}