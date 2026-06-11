package ci.nsu.mobile.calculations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.domain.CalculationsProvider
import ci.nsu.mobile.domain.DepositCalculationDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculationsViewModel(
    private val provider: CalculationsProvider,
    private val userId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculationsUiState())
    val uiState = _uiState.asStateFlow()

    private val _step1Data = MutableStateFlow<Pair<Double, Int>?>(null)
    val step1Data = _step1Data.asStateFlow()

    private val _currentCalculation = MutableStateFlow<DepositCalculationDomain?>(null)
    val currentCalculation = _currentCalculation.asStateFlow()

    init { loadCalculations() }

    private fun loadCalculations() {
        viewModelScope.launch {
            provider.getCalculationsForUser(userId).collect { list ->
                _uiState.value = _uiState.value.copy(calculations = list)
            }
        }
    }

    fun setStep1Data(amount: Double, months: Int) {
        _step1Data.value = Pair(amount, months)
    }

    fun calculate(initialAmount: Double, periodMonths: Int, rate: Double, monthlyTopUp: Double?) {
        var total = initialAmount
        for (i in 1..periodMonths) {
            total += total * rate / 100 / 12
            if (monthlyTopUp != null) total += monthlyTopUp
        }
        val interestEarned = total - initialAmount - (monthlyTopUp ?: 0.0) * periodMonths
        _currentCalculation.value = DepositCalculationDomain(
            userId = userId,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = rate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = total,
            interestEarned = interestEarned
        )
    }

    fun saveCurrentCalculation(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _currentCalculation.value?.let {
                provider.saveCalculation(it)
                onSuccess()
            }
        }
    }

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            provider.deleteCalculation(id)
        }
    }
}

data class CalculationsUiState(
    val calculations: List<DepositCalculationDomain> = emptyList()
)