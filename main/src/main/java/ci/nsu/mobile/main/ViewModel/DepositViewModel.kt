package ci.nsu.mobile.main.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Data.Local.DepositEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var selectedRate by mutableStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")

    var finalAmount by mutableStateOf(0.0)
    var interestEarned by mutableStateOf(0.0)

    val history: Flow<List<DepositEntity>> = repository.allDeposits

    var isReadOnlyMode by mutableStateOf(false)

    fun getAvailableRate(): Double {
        val months = periodMonths.toIntOrNull() ?: 0
        return when {
            months < 6 -> 15.0
            months in 6..11 -> 10.0
            else -> 5.0
        }
    }

    fun loadFromHistory(item: DepositEntity) {
        isReadOnlyMode = true
        initialAmount = item.initialAmount.toString()
        periodMonths = item.periodMonths.toString()
        monthlyTopUp = item.monthlyTopUp.toString()
        finalAmount = item.finalAmount
        interestEarned = item.interestEarned
    }

    fun calculate() {
        val initial = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val rate = getAvailableRate()
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val (total, interest) = repository.calculateDeposit(initial, months, rate, topUp)
        finalAmount = total
        interestEarned = interest
    }

    fun saveCalculation() {
        viewModelScope.launch {
            repository.insert(
                DepositEntity(
                    initialAmount = initialAmount.toDouble(),
                    periodMonths = periodMonths.toInt(),
                    interestRate = getAvailableRate(),
                    monthlyTopUp = monthlyTopUp.toDoubleOrNull() ?: 0.0,
                    finalAmount = finalAmount,
                    interestEarned = interestEarned
                )
            )
        }
    }

    fun onCalculateClicked() {
        isReadOnlyMode = false
        calculate()
    }
}