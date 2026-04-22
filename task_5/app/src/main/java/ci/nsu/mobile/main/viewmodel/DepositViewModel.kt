package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _history = MutableStateFlow<List<DepositEntity>>(emptyList())
    val history: StateFlow<List<DepositEntity>> = _history.asStateFlow()

    private val _selectedDeposit = MutableStateFlow<DepositEntity?>(null)
    val selectedDeposit: StateFlow<DepositEntity?> = _selectedDeposit.asStateFlow()

    fun loadHistory() {
        viewModelScope.launch {
            try {
                _history.value = repository.getHistory()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertDeposit(deposit: DepositEntity) {
        viewModelScope.launch {
            try {
                repository.insertDeposit(deposit)
                loadHistory() // Refresh history after saving
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadDepositById(id: Long) {
        viewModelScope.launch {
            try {
                _selectedDeposit.value = repository.getDepositById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}