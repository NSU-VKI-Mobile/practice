package ci.nsu.mobile.main.ui.step2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Step2ViewModel : ViewModel() {

    private val _availableRates = MutableLiveData<List<Pair<String, Double>>>()
    val availableRates: LiveData<List<Pair<String, Double>>> = _availableRates

    private val _selectedRate = MutableLiveData<Double>()
    val selectedRate: LiveData<Double> = _selectedRate

    private val _monthlyTopUp = MutableLiveData<Double?>()
    val monthlyTopUp: LiveData<Double?> = _monthlyTopUp

    private val _warning = MutableLiveData<String?>()
    val warning: LiveData<String?> = _warning

    fun initRates(periodMonths: Int) {
        val rates = when {
            periodMonths < 6 -> listOf("15%" to 15.0)
            periodMonths < 12 -> listOf("10%" to 10.0)
            else -> listOf("5%" to 5.0)
        }
        _availableRates.value = rates
        _selectedRate.value = rates.first().second
    }

    fun setSelectedRate(rate: Double) {
        _selectedRate.value = rate
    }

    fun setMonthlyTopUp(value: String?) {
        val clean = value?.trim()?.takeIf { it.isNotEmpty() }
        val topUp = clean?.toDoubleOrNull()
        _monthlyTopUp.value = if (topUp != null && topUp >= 0) topUp else null
    }

    fun showWarning(message: String?) {
        _warning.value = message
    }

    fun getSelectedRate(): Double = _selectedRate.value ?: 0.0
    fun getMonthlyTopUp(): Double? = _monthlyTopUp.value
}