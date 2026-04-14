package ci.nsu.mobile.main.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.utils.DepositCalculator
import kotlinx.coroutines.launch

class ResultViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _calculationResult = MutableLiveData<CalculationResult?>()
    val calculationResult: LiveData<CalculationResult?> = _calculationResult

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    fun computeAndDisplay(initialAmount: Double, periodMonths: Int, interestRate: Double, monthlyTopUp: Double?) {
        val (finalAmount, interestEarned) = DepositCalculator.calculate(
            initialAmount, periodMonths, interestRate, monthlyTopUp
        )
        _calculationResult.value = CalculationResult(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned
        )
    }

    fun saveCurrentResult() {
        val result = _calculationResult.value ?: return
        viewModelScope.launch {
            val timestamp = System.currentTimeMillis()
            val deposit = DepositCalculation(
                initialAmount = result.initialAmount,
                periodMonths = result.periodMonths,
                interestRate = result.interestRate,
                monthlyTopUp = result.monthlyTopUp,
                finalAmount = result.finalAmount,
                interestEarned = result.interestEarned,
                calculationDate = timestamp
            )
            repository.insertCalculation(deposit)
            _saveSuccess.value = true
        }
    }
}

data class CalculationResult(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double
)