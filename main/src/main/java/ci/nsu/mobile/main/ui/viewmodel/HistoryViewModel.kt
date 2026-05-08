package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val repository: DepositRepository) : ViewModel() {

    val calculations: StateFlow<List<DepositCalculation>> = repository.allCalculations
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        return repository.getById(id)
    }

    class Factory(private val repository: DepositRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                return HistoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}