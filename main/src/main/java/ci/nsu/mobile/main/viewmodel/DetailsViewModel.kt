package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(id: Long) : ViewModel() {
    private val _calculation = MutableStateFlow<DepositCalculation?>(null)
    val calculation: StateFlow<DepositCalculation?> = _calculation.asStateFlow()

    init {
        viewModelScope.launch {
            _calculation.value = DepositRepository.getCalculationById(id)
        }
    }
}