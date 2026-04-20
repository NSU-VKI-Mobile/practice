package ci.nsu.mobile.calculations.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository,
    private val currentUserId: Int
) : ViewModel() {

    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var interestRate by mutableDoubleStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")
    var finalAmount by mutableDoubleStateOf(0.0)
    var interestEarned by mutableDoubleStateOf(0.0)

    val history: Flow<List<DepositCalculation>> = if (currentUserId != -1) {
        repository.getHistory(currentUserId)
    } else { emptyFlow() }

    fun determineInterestRate(): Double {
        val months = periodMonths.toIntOrNull() ?: return 0.0
        return when {
            months < 6 -> 15.0
            months in 6..11 -> 10.0
            months >= 12 -> 5.0
            else -> 0.0
        }
    }

    fun calculateResult() {
        val amount = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val rate = interestRate
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            total += topUp
            val currentMonthInterest = total * monthlyRate
            earned += currentMonthInterest
            total += currentMonthInterest
        }
        finalAmount = total
        interestEarned = earned
    }

    fun saveCalculation() {
        val calc = DepositCalculation(
            userId = currentUserId,
            initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
            periodMonths = periodMonths.toIntOrNull() ?: 0,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp.toDoubleOrNull() ?: 0.0,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )
        viewModelScope.launch { repository.insert(calc) }
    }

    fun clearData() {
        initialAmount = ""
        periodMonths = ""
        interestRate = 0.0
        monthlyTopUp = ""
    }
}