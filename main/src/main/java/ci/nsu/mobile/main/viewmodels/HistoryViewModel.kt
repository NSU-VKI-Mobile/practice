// viewmodel/HistoryViewModel.kt
package ci.nsu.mobile.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.models.DepositCalculation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    val calculations = repository.getAllCalculations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
        }
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return repository.getCalculationById(id)
    }
}