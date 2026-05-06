package ci.nsu.mobile.main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {
    var initialAmountStr by mutableStateOf("")
    var periodMonthsStr by mutableStateOf("")
    var interestRate by mutableStateOf(0.0)
    var monthlyTopUpStr by mutableStateOf("")
    var finalAmount by mutableStateOf(0.0)
    var interestEarned by mutableStateOf(0.0)

    fun calculate() {
        val amount = initialAmountStr.toDoubleOrNull() ?: 0.0
        val months = periodMonthsStr.toIntOrNull() ?: 0
        val topUp = monthlyTopUpStr.toDoubleOrNull() ?: 0.0

        var currentTotal = amount
        val monthlyRate = (interestRate / 100) / 12.0

        for (i in 1..months) {
            currentTotal += currentTotal * monthlyRate
            currentTotal += topUp
        }

        finalAmount = currentTotal
        val totalInvested = amount + (topUp * months)
        interestEarned = finalAmount - totalInvested
    }

    fun saveResult() {
        val currentAmount = initialAmountStr.toDoubleOrNull() ?: 0.0
        val currentMonths = periodMonthsStr.toIntOrNull() ?: 0
        val currentRate = interestRate
        val currentTopUp = monthlyTopUpStr.toDoubleOrNull() ?: 0.0
        val currentFinalAmount = finalAmount
        val currentInterestEarned = interestEarned
        val currentDate = System.currentTimeMillis()

        viewModelScope.launch {
            val calc = DepositCalculation(
                initialAmount = currentAmount,
                periodMonths = currentMonths,
                interestRate = currentRate,
                monthlyTopUp = currentTopUp,
                finalAmount = currentFinalAmount,
                interestEarned = currentInterestEarned,
                calculationDate = currentDate
            )
            repository.saveResult(calc)
        }
    }
}