package ci.nsu.mobile.main.ui.step2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.model.DepositData

class Step2ViewModel : ViewModel() {

    var initialAmount: Double? = null
    var periodMonths: Int? = null

    private val _availableRates = MutableLiveData<List<Double>>()
    val availableRates: LiveData<List<Double>> = _availableRates

    private val _selectedRate = MutableLiveData<Double?>()
    val selectedRate: LiveData<Double?> = _selectedRate

    private val _monthlyTopUp = MutableLiveData<Double?>()
    val monthlyTopUp: LiveData<Double?> = _monthlyTopUp

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun setDepositData(data: DepositData) {
        initialAmount = data.initialAmount
        periodMonths = data.periodMonths
        data.monthlyTopUp?.let { _monthlyTopUp.value = it }
        updateAvailableRates()
    }

    private fun updateAvailableRates() {
        val months = periodMonths

        val rates = when {
            months == null -> {
                _errorMessage.value = "Срок вклада не указан"
                emptyList()
            }
            months < 6 -> listOf(15.0)
            months in 6..11 -> listOf(10.0)
            else -> listOf(5.0)
        }
        _availableRates.value = rates
        if (rates.isNotEmpty()) {
            _selectedRate.value = rates.first()
        }
    }

    fun setInterestRate(rate: Double) {
        _selectedRate.value = rate
    }

    fun setMonthlyTopUp(topUp: Double?) {
        _monthlyTopUp.value = topUp
    }

    fun clearError() {
        _errorMessage.value = null
    }
}