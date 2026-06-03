package ci.nsu.mobile.main.ui.secondstep

import androidx.lifecycle.ViewModel

class SecondStepViewModel : ViewModel() {

    private var initialAmount: Double = 0.0
    private var periodMonths: Int = 0
    private var interestRate: Double = 0.0
    private var monthlyTopUp: Double? = null
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
        this.initialAmount = initialAmount
        this.periodMonths = periodMonths
        this.interestRate = interestRate
        this.monthlyTopUp = monthlyTopUp
        this.finalAmount = finalAmount
        this.interestEarned = interestEarned

        android.util.Log.d("SecondStepViewModel", "Saved: initialAmount=$initialAmount, periodMonths=$periodMonths, interestRate=$interestRate, finalAmount=$finalAmount")
    }

    fun getInitialAmount(): Double = initialAmount
    fun getPeriodMonths(): Int = periodMonths
    fun getInterestRate(): Double = interestRate
    fun getMonthlyTopUp(): Double? = monthlyTopUp
    fun getFinalAmount(): Double = finalAmount
    fun getInterestEarned(): Double = interestEarned
}