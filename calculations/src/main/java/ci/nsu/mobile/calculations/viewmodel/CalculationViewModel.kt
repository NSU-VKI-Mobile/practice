package ci.nsu.mobile.calculations.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.calculations.domain.DepositCalculator
import ci.nsu.mobile.domain.model.CalculationResult

class CalculationViewModel : ViewModel() {
    fun calculatePreview(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double = 0.0
    ): CalculationResult {
        return DepositCalculator.calculate(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp
        )
    }
}
