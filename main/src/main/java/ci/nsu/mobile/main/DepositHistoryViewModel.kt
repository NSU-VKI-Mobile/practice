package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DepositHistoryViewModel(private val repository: DepositRepository) : ViewModel() {

    val calculations: Flow<List<DepositCalculation>> = repository.calculations

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteCalculationById(calculation.id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAllCalculations()
        }
    }
}
