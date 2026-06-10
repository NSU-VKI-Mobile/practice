package ci.nsu.mobile.main.ui.step1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Step1ViewModel : ViewModel() {

    private val _initialAmount = MutableLiveData<Double?>()
    val initialAmount: LiveData<Double?> = _initialAmount

    private val _periodMonths = MutableLiveData<Int?>()
    val periodMonths: LiveData<Int?> = _periodMonths

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun updateInitialAmount(value: String) {
        val amount = value.toDoubleOrNull()
        _initialAmount.value = amount
        validate()
    }

    fun updatePeriodMonths(value: String) {
        val months = value.toIntOrNull()
        _periodMonths.value = months
        validate()
    }

    private fun validate() {
        val amount = _initialAmount.value
        val months = _periodMonths.value

        _error.value = when {
            amount == null -> "Введите стартовый взнос"
            amount <= 0.0 -> "Стартовый взнос должен быть больше 0"
            months == null -> "Введите срок в месяцах"
            months <= 0 -> "Срок должен быть больше 0 месяцев"
            else -> null
        }
    }

    fun isInputValid(): Boolean = _error.value == null
}