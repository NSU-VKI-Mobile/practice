package ci.nsu.moble.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import ci.nsu.moble.main.domain.models.CalculationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculationViewModel : ViewModel() {
    private val _state = MutableStateFlow(CalculationState())
    val state: StateFlow<CalculationState> = _state.asStateFlow()

    fun setStep1(amount: Double, months: Int) {
        _state.value = _state.value.copy(initialAmount = amount, periodMonths = months)
    }

    fun setStep2(rate: Double, topUp: Double?) {
        _state.value = _state.value.copy(interestRate = rate, monthlyTopUp = topUp)
    }
}