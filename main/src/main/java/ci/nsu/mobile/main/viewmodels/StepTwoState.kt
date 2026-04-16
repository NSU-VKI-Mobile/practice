package ci.nsu.mobile.main.viewmodels

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class StepTwoState(
    val selectedInterestRate: Double? = null,
    val monthlyTopUp: String = "",
    val monthlyTopUpError: String? = null,
    val availableRates: List<Double> = emptyList()
)

class StepTwoViewModel : ViewModel() {
    private val _state = MutableStateFlow(StepTwoState())
    val state: StateFlow<StepTwoState> = _state.asStateFlow()

    fun initializeRates(periodMonths: Int) {
        val rates = listOfNotNull(DepositRepository.getInterestRate(periodMonths))
        _state.update {
            it.copy(
                availableRates = rates,
                selectedInterestRate = rates.firstOrNull()
            )
        }
    }

    fun updateSelectedRate(rate: Double) {
        _state.update { it.copy(selectedInterestRate = rate) }
    }

    fun updateMonthlyTopUp(value: String) {
        _state.update {
            it.copy(
                monthlyTopUp = value,
                monthlyTopUpError = validateMonthlyTopUp(value)
            )
        }
    }

    private fun validateMonthlyTopUp(value: String): String? {
        if (value.isBlank()) return null
        val amount = value.toDoubleOrNull()
        if (amount == null) return "Введите корректное число"
        if (amount < 0) return "Сумма не может быть отрицательной"
        if (amount > 1_000_000) return "Сумма слишком велика"
        return null
    }

    fun isValid(): Boolean = _state.value.monthlyTopUpError == null

    fun getSelectedRate(): Double? = _state.value.selectedInterestRate
    fun getMonthlyTopUp(): Double? = _state.value.monthlyTopUp.toDoubleOrNull()
}