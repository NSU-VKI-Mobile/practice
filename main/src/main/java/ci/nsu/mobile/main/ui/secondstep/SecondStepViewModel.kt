package ci.nsu.mobile.main.ui.secondstep

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.mai.database.DepositData

class SecondStepViewModel : ViewModel() {

    private var depositData: DepositData? = null
    private var finalAmount: Double = 0.0
    private var interestEarned: Double = 0.0

    fun saveResult(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?,
        finalAmount: Double,
        interestEarned: Double
    ) {
        depositData = DepositData(initialAmount, periodMonths, interestRate, monthlyTopUp)
        this.finalAmount = finalAmount
        this.interestEarned = interestEarned
    }

    fun getDepositData(): DepositData? = depositData
    fun getFinalAmount(): Double = finalAmount
    fun getInterestEarned(): Double = interestEarned
}