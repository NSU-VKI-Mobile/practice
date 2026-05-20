package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.DepositEntity
import ci.nsu.moble.main.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow
import java.math.BigDecimal
import java.math.RoundingMode

data class DepositState(
    val initialAmount: String = "",
    val months: String = "",
    val rate: Double = 0.0,
    val monthly: String = "",
    val finalAmount: Double = 0.0,
    val interest: Double = 0.0,

    // UI
    val error: String? = null
)

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DepositState())
    val state = _state

    val history = repository.allDeposits

    fun setInitial(v: String) {
        _state.value = _state.value.copy(initialAmount = v)
    }

    fun setMonths(v: String) {
        _state.value = _state.value.copy(months = v)
    }

    fun setMonthly(v: String) {
        _state.value = _state.value.copy(monthly = v)
    }

    fun setRate(rate: Double) {
        _state.value = _state.value.copy(rate = rate)
    }

    fun setError(msg: String?) {
        _state.value = _state.value.copy(error = msg)
    }

    fun calculateRate() {
        val m = _state.value.months.toIntOrNull() ?: return

        val rate = when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }

        setRate(rate)
    }

    fun calculate() {
        val amount = _state.value.initialAmount.toDoubleOrNull() ?: return
        val months = _state.value.months.toIntOrNull() ?: return
        val rate = _state.value.rate / 100
        val monthly = _state.value.monthly.toDoubleOrNull() ?: 0.0

        var total = amount

        repeat(months) {
            total += monthly
            total *= (1 + rate / 12)
        }

        val interest = total - (amount + monthly * months)

        _state.value = _state.value.copy(
            finalAmount = total,
            interest = interest
        )
    }

    fun save() {
        val s = _state.value

        viewModelScope.launch {
            repository.insert(
                DepositEntity(
                    initialAmount = s.initialAmount.toDoubleOrNull() ?: 0.0,
                    periodMonths = s.months.toIntOrNull() ?: 0,
                    interestRate = s.rate,
                    monthlyTopUp = s.monthly.toDoubleOrNull(),
                    finalAmount = s.finalAmount,
                    interestEarned = s.interest,
                    calculationDate = System.currentTimeMillis()
                )
            )
        }
    }

    fun clear(){
        _state.value = DepositState()
    }

    fun deleteDeposit(item: DepositEntity) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}