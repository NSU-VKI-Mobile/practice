package ci.nsu.mobile.main.ViewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Database.AppDatabase
import ci.nsu.mobile.main.Database.DepositCalculation
import ci.nsu.mobile.main.Database.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository
    val history: LiveData<List<DepositCalculation>>

    init {
        val depositDao = AppDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(depositDao)
        history = repository.allCalculations
    }

    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var interestRate by mutableDoubleStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")

    var finalAmount by mutableDoubleStateOf(0.0)
    var interestEarned by mutableDoubleStateOf(0.0)

    fun updateInterestRate(rate: Double) {
        interestRate = rate
    }

    private var _selectedCalculation by mutableStateOf<DepositCalculation?>(null)

    fun selectCalculation(calculation: DepositCalculation) {
        _selectedCalculation = calculation
    }

    fun determineInterestRate(): Double {
        val months = periodMonths.toIntOrNull() ?: return 0.0
        return when {
            months < 6 -> 15.0
            months in 6..11 -> 10.0
            months >= 12 -> 5.0
            else -> 0.0
        }
    }

    fun calculateResult() {
        val amount = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val rate = interestRate
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            total += topUp
            val currentMonthInterest = total * monthlyRate
            earned += currentMonthInterest
            total += currentMonthInterest
        }

        finalAmount = total
        interestEarned = earned
    }

    fun saveCalculation() {
        val calc = DepositCalculation(
            initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
            periodMonths = periodMonths.toIntOrNull() ?: 0,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp.toDoubleOrNull() ?: 0.0,
            finalAmount = finalAmount,
            interestEarned = interestEarned
        )
        viewModelScope.launch {
            repository.insert(calc)
        }
    }

    fun clearData() {
        initialAmount = ""
        periodMonths = ""
        interestRate = 0.0
        monthlyTopUp = ""
    }
}