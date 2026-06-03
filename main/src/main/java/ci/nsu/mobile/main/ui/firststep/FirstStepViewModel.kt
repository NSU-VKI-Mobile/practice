package ci.nsu.mobile.main.ui.firststep

import androidx.lifecycle.ViewModel

class FirstStepViewModel : ViewModel() {

    private var initialAmount: Double = 0.0
    private var periodMonths: Int = 0

    fun saveInitialData(amount: Double, period: Int) {
        initialAmount = amount
        periodMonths = period
        android.util.Log.d("FirstStepViewModel", "Saved: amount=$amount, period=$period")
    }

    fun getInitialAmount(): Double {
        android.util.Log.d("FirstStepViewModel", "getInitialAmount: $initialAmount")
        return initialAmount
    }

    fun getPeriodMonths(): Int {
        android.util.Log.d("FirstStepViewModel", "getPeriodMonths: $periodMonths")
        return periodMonths
    }
}