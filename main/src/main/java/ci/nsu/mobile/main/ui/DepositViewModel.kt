package ci.nsu.mobile.main.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.repository.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    val history = repository.allCalculations

    // Состояния полей ввода
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")

    // Новое: выбранная ставка из списка
    var selectedRate by mutableStateOf("")

    // Новое: выбранный элемент для экрана деталей
    var selectedCalculation by mutableStateOf<DepositCalculation?>(null)

    // Результаты расчета
    var finalAmount by mutableStateOf(0.0)
    var interestEarned by mutableStateOf(0.0)
    var currentRate by mutableStateOf(0.0)

    // Возвращает список доступных ставок (в нашем случае по условию это 1 ставка,
    // но список нужен для работы DropdownMenu)
    fun getAvailableRates(): List<Double> {
        val months = periodMonths.toIntOrNull() ?: return emptyList()
        return when {
            months < 6 -> listOf(15.0)
            months in 6..11 -> listOf(10.0)
            else -> listOf(5.0)
        }
    }

    fun calculateDeposit() {
        val initial = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        // Берём выбранную ставку
        val rate = selectedRate.toDoubleOrNull() ?: getAvailableRates().firstOrNull() ?: 0.0

        currentRate = rate
        var balance = initial
        var totalInterest = 0.0

        for (i in 1..months) {
            val interestForMonth = balance * (rate / 100 / 12)
            totalInterest += interestForMonth
            balance += interestForMonth + topUp
        }

        finalAmount = balance
        interestEarned = totalInterest
    }

    fun saveCalculation() {
        viewModelScope.launch {
            val calc = DepositCalculation(
                initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
                periodMonths = periodMonths.toIntOrNull() ?: 0,
                interestRate = currentRate,
                monthlyTopUp = monthlyTopUp.toDoubleOrNull() ?: 0.0,
                finalAmount = finalAmount,
                interestEarned = interestEarned
            )
            repository.insert(calc)
        }
    }

    fun resetData() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = ""
    }
}