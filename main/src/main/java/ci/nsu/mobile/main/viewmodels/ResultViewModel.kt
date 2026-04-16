package ci.nsu.mobile.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.models.DepositCalculation
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    fun saveCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?,
        finalAmount: Double,
        interestEarned: Double,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val calculation = DepositCalculation(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned
            )
            repository.insertCalculation(calculation)
            onSuccess()
        }
    }
}