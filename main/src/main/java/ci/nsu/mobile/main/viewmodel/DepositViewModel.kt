package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    // История расчётов из базы
    val calculations: Flow<List<DepositCalculation>> =
        repository.allCalculations

    // Сохранение расчёта
    fun saveCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.insert(calculation)
        }
    }

    // Очистка всей истории
    fun clearHistory() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}