package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _calculations = MutableStateFlow<List<DepositEntity>>(emptyList())
    val calculations: StateFlow<List<DepositEntity>> = _calculations.asStateFlow()

    init {
        loadCalculations()
    }

    fun loadCalculations() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _calculations.value = list
            }
        }
    }

    fun addCalculation(calculation: DepositEntity) {
        viewModelScope.launch {
            repository.saveCalculation(calculation)
            // Не перезагружаем сразу - база сама обновит
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadCalculations()
        }
    }
}