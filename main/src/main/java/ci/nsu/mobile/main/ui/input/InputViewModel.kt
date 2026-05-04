package ci.nsu.mobile.main.ui.input

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.entity.CalculationInput
import ci.nsu.mobile.main.util.DepositCalculator
import ci.nsu.mobile.main.util.ValidationResult
import ci.nsu.mobile.main.util.Validator

class InputViewModel : ViewModel() {

    val initialAmount = MutableLiveData<String>("")
    val periodMonths = MutableLiveData<String>("")
    val interestRate = MutableLiveData<String>("")
    val monthlyTopUp = MutableLiveData<String>("")

    fun selectRateForPeriod(months: Int): Double {
        return DepositCalculator.selectRate(months)
    }

    fun validateStep1(): ValidationResult {
        val amountResult = Validator.validateInitialAmount(initialAmount.value ?: "")
        if (amountResult is ValidationResult.Invalid) return amountResult
        return Validator.validatePeriodMonths(periodMonths.value ?: "")
    }

    fun validateStep2(): ValidationResult {
        return Validator.validateMonthlyTopUp(monthlyTopUp.value ?: "")
    }

    fun buildCalculationInput(): CalculationInput {
        return CalculationInput(
            initialAmount = initialAmount.value?.toDoubleOrNull() ?: 0.0,
            periodMonths = periodMonths.value?.toIntOrNull() ?: 0,
            interestRate = interestRate.value?.toDoubleOrNull() ?: 0.0,
            monthlyTopUp = monthlyTopUp.value?.toDoubleOrNull() ?: 0.0
        )
    }
}
