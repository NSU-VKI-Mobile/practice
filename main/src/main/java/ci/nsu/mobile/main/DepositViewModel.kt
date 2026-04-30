package ci.nsu.mobile.main
import ci.nsu.mobile.main.domain.DepositCalculator


import androidx.lifecycle.*
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.local.DepositRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository, private val calculator: DepositCalculator = DepositCalculator()) : ViewModel() {

    val allCalculations: StateFlow<List<DepositCalculation>> = repository.allCalculations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun calculateAndSave(amount: Double, months: Int, topUp: Double, rate: Double) {
        viewModelScope.launch {
            val newRecord = calculator.calculate(amount, months, topUp)
            repository.insert(newRecord)
        }
    }

    class Factory(private val repository: DepositRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DepositViewModel(repository) as T
        }
    }
}