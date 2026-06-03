package ci.nsu.mobile.main.ui.firststep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstStepViewModel : ViewModel() {

    private val _initialAmount = MutableLiveData<Double>()
    val initialAmount: LiveData<Double> = _initialAmount

    private val _periodMonths = MutableLiveData<Int>()
    val periodMonths: LiveData<Int> = _periodMonths

    fun saveInitialData(amount: Double, period: Int) {
        _initialAmount.value = amount
        _periodMonths.value = period
    }

    fun getInitialAmount(): Double = _initialAmount.value ?: 0.0
    fun getPeriodMonths(): Int = _periodMonths.value ?: 0
}