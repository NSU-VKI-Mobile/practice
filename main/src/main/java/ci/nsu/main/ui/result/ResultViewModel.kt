package ci.nsu.mobile.main.ui.result

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.model.DepositCalculation
import ci.nsu.mobile.main.model.DepositData

class ResultViewModel : ViewModel() {

    private val repository = DepositApplication.getInstance().repository
    private var currentCalculation: DepositCalculation? = null

    fun setCalculationData(data: DepositData, finalAmount: Double, interestEarned: Double) {
        currentCalculation = DepositCalculation(
            initialAmount = data.initialAmount ?: 0.0,
            periodMonths = data.periodMonths ?: 0,
            interestRate = data.interestRate ?: 0.0,
            monthlyTopUp = data.monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned
        )
    }

    suspend fun saveCalculation(calculation: DepositCalculation): Boolean {
        return try {
            repository.saveCalculation(calculation)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}