package ci.nsu.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.data.repository.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {
    var history by mutableStateOf<List<DepositCalculation>>(emptyList())
        private set

    fun load(login: String) {
        viewModelScope.launch {
            history = repository.getHistory(login)
        }
    }

    fun add(item: DepositCalculation, login: String) {
        viewModelScope.launch {
            repository.insertDeposit(item)
            load(login)
        }
    }

    fun delete(item: DepositCalculation, login: String) {
        viewModelScope.launch {
            repository.deleteDeposit(item)
            load(login)
        }
    }

    fun clearHistory(userLogin: String) {
        viewModelScope.launch {
            repository.clearAll()
            history = emptyList()
        }
    }
}