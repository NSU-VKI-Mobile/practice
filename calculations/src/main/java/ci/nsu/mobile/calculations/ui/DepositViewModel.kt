package ci.nsu.mobile.calculations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.domain.calculations.CalculationsProvider
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    private val provider: CalculationsProvider,
    private val currentUserId: Long
) : ViewModel() {

    val calculations: StateFlow<List<DepositCalculation>> = provider.getCalculationsForUser(currentUserId)
        .stateIn(viewModelScope, started = kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())

    private val _calcResult = MutableStateFlow<DepositCalculation?>(null)
    val calcResult: StateFlow<DepositCalculation?> = _calcResult.asStateFlow()

    fun calculateDeposit(amount: Double, months: Int, rate: Double, topUp: Double) {
        val calculation = provider.calculateDeposit(amount, months, rate, topUp, currentUserId)
        _calcResult.value = calculation
    }

    fun saveCalculation(calc: DepositCalculation) {
        viewModelScope.launch {
            provider.saveCalculation(calc)
            _calcResult.value = null
        }
    }

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            provider.deleteCalculation(id)
        }
    }
}