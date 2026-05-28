package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.launch

class DepositResultViewModel(private val repository: DepositRepository) : ViewModel() {

    fun calculateFinalAmount(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double
    ): Double {
        var amount = initialAmount
        val monthlyRate = interestRate / 100 / 12

        for (month in 1..periodMonths) {
            amount += amount * monthlyRate
            amount += monthlyTopUp
        }

        return amount
    }

    fun saveCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double,
        finalAmount: Double,
        interestEarned: Double,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            val calculation = DepositCalculation(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                calculationDate = System.currentTimeMillis()
            )

            repository.saveCalculation(calculation)
            onSaved()
        }
    }
}
