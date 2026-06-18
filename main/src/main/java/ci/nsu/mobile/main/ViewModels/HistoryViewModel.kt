package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import ci.nsu.mobile.main.Data.Repository.CalculationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: CalculationRepository) : ViewModel() {

    private val _calculations = MutableStateFlow<List<CalculationEntity>>(emptyList())
    val calculations: StateFlow<List<CalculationEntity>> = _calculations.asStateFlow()

    init {
        loadCalculations()
    }

    private fun loadCalculations() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _calculations.value = list
            }
        }
    }
}