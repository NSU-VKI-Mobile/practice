package ci.nsu.mobile.main.ui.secondstep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondStepViewModel : ViewModel() {

    private val _availableRates = MutableLiveData<List<Double>>()
    val availableRates: LiveData<List<Double>> = _availableRates

    private val _selectedRate = MutableLiveData<Double?>()
    val selectedRate: LiveData<Double?> = _selectedRate

    private val _monthlyTopUp = MutableLiveData<String>()
    val monthlyTopUp: LiveData<String> = _monthlyTopUp

    fun loadRates(periodMonths: Int) {
        val rates = when {
            periodMonths < 6 -> listOf(15.0)
            periodMonths < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
        _availableRates.value = rates
        _selectedRate.value = rates.firstOrNull()
    }

    fun setSelectedRate(rate: Double) {
        _selectedRate.value = rate
    }

    fun updateMonthlyTopUp(value: String) {
        _monthlyTopUp.value = value
    }

    fun getMonthlyTopUpAsDouble(): Double? {
        val text = _monthlyTopUp.value
        return if (text.isNullOrBlank()) null else text.toDoubleOrNull()
    }
}