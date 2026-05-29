package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository
    val allCalculationsUnit = AppDatabase.getDatabase(application).depositDao()

    init {
        repository = DepositRepository(allCalculationsUnit)
    }

    val historyState = repository.allCalculations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Состояния для ввода на Этапе 1 и 2
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")
    var selectedRate by mutableStateOf(15.0)

    // Состояние для отображения результата расчёта
    var currentResult by mutableStateOf<DepositCalculation?>(null)
    var selectedHistoryItem by mutableStateOf<DepositCalculation?>(null)

    // Автоматический выбор ставки в зависимости от срока
    fun updateRateBasedOnPeriod() {
        val months = periodMonths.toIntOrNull() ?: return
        selectedRate = when {
            months < 6 -> 15.0
            months in 6..11 -> 10.0
            else -> 5.0
        }
    }

    // Логика финансового расчёта (капитализация процентов каждый месяц)
    fun calculateDeposit() {
        val initial = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0
        val rate = selectedRate

        var finalAmount = initial
        val monthlyRate = rate / 12 / 100

        for (i in 1..months) {
            finalAmount += topUp
            finalAmount *= (1 + monthlyRate)
        }

        val totalInvested = initial + (topUp * months)
        val interestEarned = finalAmount - totalInvested

        currentResult = DepositCalculation(
            initialAmount = initial,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = Math.round(finalAmount * 100) / 100.0,
            interestEarned = Math.round(interestEarned * 100) / 100.0
        )
    }

    fun saveResultToDb() {
        currentResult?.let {
            viewModelScope.launch {
                repository.insert(it)
            }
        }
    }

    fun resetInputs() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = 15.0
        currentResult = null
    }
}