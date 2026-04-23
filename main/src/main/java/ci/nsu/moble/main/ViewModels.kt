package ci.nsu.moble.main

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


data class Step1State(
    val amount: String = "",
    val months: String = "",
    val amountError: Boolean = false,
    val monthsError: Boolean = false
)

class Step1ViewModel : ViewModel() {
    private val _state = MutableStateFlow(Step1State())
    val state: StateFlow<Step1State> = _state.asStateFlow()

    fun setAmount(value: String) {
        _state.update {
            it.copy(
                amount = value,
                amountError = value.toDoubleOrNull()?.let { it <= 0 } ?: true
            )
        }
    }

    fun setMonths(value: String) {
        _state.update {
            it.copy(
                months = value,
                monthsError = value.toIntOrNull()?.let { it <= 0 } ?: true
            )
        }
    }

    fun getValidatedData(): Pair<Double, Int>? {
        val current = _state.value
        val amount = current.amount.toDoubleOrNull()
        val months = current.months.toIntOrNull()
        if (amount != null && amount > 0 && months != null && months > 0) {
            return Pair(amount, months)
        }
        _state.update {
            it.copy(
                amountError = amount == null || amount <= 0,
                monthsError = months == null || months <= 0
            )
        }
        return null
    }
}


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
                _state.update { it.copy(showRateWarning = true, availableRates = emptyList()) }
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


class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as DepositApp).repository
    val calculations: StateFlow<List<DepositCalculation>> =
        repository.getAllCalculations()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as DepositApp).repository
    private val _calculation = MutableStateFlow<DepositCalculation?>(null)
    val calculation: StateFlow<DepositCalculation?> = _calculation.asStateFlow()

    fun loadCalculation(id: Long) {
        viewModelScope.launch {
            _calculation.value = repository.getCalculationById(id)
        }
    }
}