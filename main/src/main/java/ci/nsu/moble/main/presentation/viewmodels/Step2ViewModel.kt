package ci.nsu.moble.main.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Step2State(
    val interestRate: Double? = null,
    val monthlyTopUp: String = "",
    val availableRates: List<Double> = emptyList(),
    val showRateWarning: Boolean = false,
    val topUpError: Boolean = false
)

class Step2ViewModel : ViewModel() {
    private val _state = MutableStateFlow(Step2State())
    val state: StateFlow<Step2State> = _state.asStateFlow()

    fun setPeriod(months: Int?) {
        val rates = when {
            months == null || months <= 0 -> {
                _state.update {
                    it.copy(
                        showRateWarning = true,
                        availableRates = emptyList(),
                        interestRate = null
                    )
                }
                return
            }
            months < 6 -> listOf(15.0)
            months in 6..11 -> listOf(10.0)
            else -> listOf(5.0)
        }
        _state.update {
            it.copy(
                availableRates = rates,
                showRateWarning = false,
                interestRate = rates.first()
            )
        }
    }

    fun setRate(rate: Double) = _state.update { it.copy(interestRate = rate) }

    fun setTopUp(value: String) {
        val error = value.isNotEmpty() && (value.toDoubleOrNull() == null || value.toDouble()!! < 0)
        _state.update { it.copy(monthlyTopUp = value, topUpError = error) }
    }

    fun getValidatedData(): Pair<Double, Double?>? {
        val current = _state.value
        return if (current.interestRate != null && !current.topUpError) {
            Pair(current.interestRate!!, current.monthlyTopUp.toDoubleOrNull())
        } else null
    }
}