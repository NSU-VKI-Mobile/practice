package ci.nsu.mobile.main.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositEntity
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {
    var initialAmount by mutableStateOf("")
    var months by mutableStateOf("")
    var rate by mutableDoubleStateOf(0.0)
    var monthlyTopUp by mutableStateOf("0")

    var finalAmount by mutableDoubleStateOf(0.0)
    var interestEarned by mutableDoubleStateOf(0.0)

    val history = repository.allDeposits.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun calculate() {
        val p = initialAmount.toDoubleOrNull() ?: 0.0
        val n = months.toIntOrNull() ?: 0
        val r = rate / 100 / 12
        val m = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val amount = p * (1 + r).pow(n.toDouble()) +  /*расчет роста начального взноса*/
                if (r > 0) m * (((1 + r).pow(n.toDouble()) - 1) / r) else m * n /*расчет роста ежемесячных пополнений*/

        finalAmount = amount
        interestEarned = amount - p - (m * n) /*чистая прибыль*/
    }

    fun saveToDb() {
        viewModelScope.launch {
            if (initialAmount.isNotBlank() && months.isNotBlank()) {
                repository.insert(DepositEntity(
                    initialAmount = initialAmount.toDouble(),
                    months = months.toInt(),
                    rate = rate,
                    monthlyTopUp = monthlyTopUp.toDouble(),
                    finalAmount = finalAmount,
                    interestEarned = interestEarned
                ))
            }
        }
    }

    fun reset() {
        initialAmount = ""; months = ""; rate = 0.0; monthlyTopUp = "0"
        finalAmount = 0.0; interestEarned = 0.0
    }

    fun updateMonthlyTopUp(input: String) {
        // Если в поле был "0" и вводится цифра, заменяем 0 на эту цифру
        val cleanedInput = if (monthlyTopUp == "0" && input.length > 1 && input.all { it.isDigit() }) {
            input.removePrefix("0")
        } else {
            input
        }

        // Разрешаем только цифры (или пустую строку)
        if (cleanedInput.isEmpty() || cleanedInput.all { it.isDigit() }) {
            monthlyTopUp = cleanedInput
        }
    }

    fun updateInitialAmount(input: String) {
        // Разрешаем цифры и один разделитель (точку или запятую)
        if (input.isEmpty() || input.matches(Regex("""^\d*[.,]?\d*$"""))) {
            initialAmount = input
        }
    }

    fun updateMonths(input: String) {
        // Только цифры
        if (input.isEmpty() || input.all { it.isDigit() }) {
            months = input
        }
    }
}