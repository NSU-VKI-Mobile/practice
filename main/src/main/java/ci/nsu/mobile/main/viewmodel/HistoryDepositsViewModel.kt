package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculationEntity
import ci.nsu.mobile.main.domain.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryDepositsViewModel(val repos: DepositRepository): ViewModel(){
    private val _historyState = MutableStateFlow<List<DepositCalculationEntity>>(emptyList())
    val historyState: StateFlow<List<DepositCalculationEntity>> = _historyState.asStateFlow()

    private val _selectedState = MutableStateFlow<DepositCalculationEntity?>(null)
    val selectedState: StateFlow<DepositCalculationEntity?> = _selectedState.asStateFlow()
    init {
        loadHistory()
    }
    fun selectedDepositUpdate(deposit: DepositCalculationEntity) {
        _selectedState.value = deposit
    }
    fun loadHistory() {
        viewModelScope.launch {
            repos.getAll().collect { deposits -> _historyState.value = deposits }
        }
    }
}