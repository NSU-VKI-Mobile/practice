package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import ci.nsu.mobile.main.Data.Repository.CalculationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: CalculationRepository, private val calculationId: Long
) : ViewModel() {

    private val _calculation = MutableStateFlow<CalculationEntity?>(null)
    val calculation: StateFlow<CalculationEntity?> = _calculation.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCalculationById(calculationId).collect { calc ->
                _calculation.value = calc
            }
        }
    }
}