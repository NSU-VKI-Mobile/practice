package ci.nsu.mobile.main.ui.firststep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstStepViewModel : ViewModel() {
    private val _initialAmount = MutableLiveData<String>()
    val initialAmount: LiveData<String> = _initialAmount

    private val _periodMonths = MutableLiveData<String>()
    val periodMonths: LiveData<String> = _periodMonths

    private val _isValid = MutableLiveData<Boolean>()
    val isValid: LiveData<Boolean> = _isValid

    fun updateInitialAmount(value: String) {
        _initialAmount.value = value
        validate()
    }

    fun updatePeriodMonths(value: String) {
        _periodMonths.value = value
        validate()
    }

    private fun validate() {
        val amount = _initialAmount.value?.toDoubleOrNull()
        val months = _periodMonths.value?.toIntOrNull()
        _isValid.value = (amount != null && amount > 0) && (months != null && months > 0)
    }
}