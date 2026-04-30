package ci.nsu.mobile.main.ui.step1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.model.DepositData

class Step1ViewModel : ViewModel() {

    private val _initialAmount = MutableLiveData<Double?>()
    val initialAmount: LiveData<Double?> = _initialAmount

    private val _periodMonths = MutableLiveData<Int?>()
    val periodMonths: LiveData<Int?> = _periodMonths

    fun setInitialAmount(amount: Double) {
        _initialAmount.value = amount
    }

    fun setPeriodMonths(months: Int) {
        _periodMonths.value = months
    }

    fun restoreData(data: DepositData) {
        data.initialAmount?.let { _initialAmount.value = it }
        data.periodMonths?.let { _periodMonths.value = it }
    }
}