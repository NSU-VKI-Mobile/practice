package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _initialAmount = MutableStateFlow("")
    val initialAmount: StateFlow<String> = _initialAmount

    private val _months = MutableStateFlow("")
    val months: StateFlow<String> = _months

    private val _topUp = MutableStateFlow("")
    val topUp: StateFlow<String> = _topUp

    private val _rate = MutableStateFlow(0.0)
    val rate: StateFlow<Double> = _rate

    val history = repository.getAll()

    fun setInitialAmount(value: String) {
        _initialAmount.value = value
    }

    fun setMonths(value: String) {
        _months.value = value
    }

    fun setTopUp(value: String) {
        _topUp.value = value
    }

    fun calculateRate() {
        val m = _months.value.toIntOrNull() ?: return
        _rate.value = when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }
    }

    fun calculateResult(): Pair<Double, Double> {
        val initial = _initialAmount.value.toDoubleOrNull() ?: 0.0
        val months = _months.value.toIntOrNull() ?: 0
        val rate = _rate.value / 100
        val topUp = _topUp.value.toDoubleOrNull() ?: 0.0

        var total = initial

        repeat(months) {
            total += total * rate / 12
            total += topUp
        }

        val interest = total - initial - (topUp * months)

        return total to interest
    }

    fun save() {
        val (total, interest) = calculateResult()

        val entity = DepositEntity(
            initialAmount = _initialAmount.value.toDouble(),
            months = _months.value.toInt(),
            rate = _rate.value,
            topUp = _topUp.value.toDoubleOrNull() ?: 0.0,
            finalAmount = total,
            interest = interest,
            date = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insert(entity)
        }
    }

    fun setRate(toDouble: Double) {}
}