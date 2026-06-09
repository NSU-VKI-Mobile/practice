package ci.nsu.mobile.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.launch

class Stage03ViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    fun saveCalculation(
        initialDeposit: Double,
        termMonths: Int,
        interestRate: Double,
        finalAmount: Double,
        interestEarned: Double,
        depositName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val calculation = DepositCalculation(
                    initialAmount = initialDeposit,
                    periodMonths = termMonths,
                    interestRate = interestRate,
                    finalAmount = finalAmount,
                    interestEarned = interestEarned,
                    depositName = depositName,
                    calculationDate = System.currentTimeMillis()
                )
                repository.insertCalculation(calculation)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}