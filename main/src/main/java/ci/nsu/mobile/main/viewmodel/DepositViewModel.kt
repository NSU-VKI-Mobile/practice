package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository

    private val _history = MutableStateFlow<List<DepositEntity>>(emptyList())
    val history: StateFlow<List<DepositEntity>> = _history

    // временные данные расчёта
    var startAmount: String = ""
    var months: String = ""
    var percent: String = ""
    var topUp: String = ""

    init {
        val dao = DepositDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(dao)
        loadHistory()
    }

    // ---------- CALCULATION ----------

    fun calculateFinal(): Float {
        val p = percent.toDoubleOrNull() ?: 0.0
        val amount = startAmount.toDoubleOrNull() ?: 0.0
        val monthsInt = months.toIntOrNull() ?: 0
        val topUpVal = topUp.toDoubleOrNull() ?: 0.0

        var result = amount

        for (i in 1..monthsInt) {
            result += topUpVal
            result += result * (p / 100.0 / 12.0)
        }

        return result.toFloat()
    }

    fun calculateEarned(): Float {
        val final = calculateFinal()

        val start = startAmount.toDoubleOrNull() ?: 0.0
        val topUpVal = topUp.toDoubleOrNull() ?: 0.0
        val monthsInt = months.toIntOrNull() ?: 0

        val invested = start + (topUpVal * monthsInt)

        return (final - invested).toFloat()
    }

    // ---------- DB ----------

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getHistory()
        }
    }

    fun saveCalculation(finalAmount: Float, earned: Float) {
        viewModelScope.launch {
            val entity = DepositEntity(
                initialAmount = startAmount.toFloatOrNull() ?: 0f,
                periodMonths = months.toIntOrNull() ?: 0,
                interestRate = percent.toDoubleOrNull() ?: 0.0,
                monthlyTopUp = topUp.toFloatOrNull() ?: 0f,
                finalAmount = finalAmount,
                interestEarned = earned,
                calculationDate = System.currentTimeMillis()
            )

            repository.insertDeposit(entity)
            loadHistory()
        }
    }
}