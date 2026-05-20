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

    fun calculateFinal(): Int {
        val p = percent.toDoubleOrNull() ?: 0.0
        val amount = startAmount.toIntOrNull() ?: 0
        val monthsInt = months.toIntOrNull() ?: 0
        val topUpInt = topUp.toIntOrNull() ?: 0

        var result = amount.toDouble()

        for (i in 1..monthsInt) {
            result += topUpInt
            result += result * (p / 100 / 12)
        }

        return result.toInt()
    }

    fun calculateEarned(): Int {
        val final = calculateFinal()
        val start = startAmount.toIntOrNull() ?: 0
        val topUpInt = topUp.toIntOrNull() ?: 0
        val monthsInt = months.toIntOrNull() ?: 0

        val invested = start + (topUpInt * monthsInt)
        return final - invested
    }

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getHistory()
        }
    }

    fun saveCalculation(finalAmount: Int, earned: Int) {
        viewModelScope.launch {
            val entity = DepositEntity(
                initialAmount = startAmount.toIntOrNull() ?: 0,
                periodMonths = months.toIntOrNull() ?: 0,
                interestRate = percent.toDoubleOrNull() ?: 0.0,
                monthlyTopUp = topUp.toIntOrNull() ?: 0,
                finalAmount = finalAmount,
                interestEarned = earned,
                calculationDate = System.currentTimeMillis()
            )

            repository.insertDeposit(entity)
            loadHistory()
        }
    }
}