package ci.nsu.mobile.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainViewModel(private val repository: DepositRepository) : ViewModel() {

    var initialAmount: Int = 0
    var periodMonths: Int = 0
    var interestRate: Double = 0.0
    var monthlyTopUp: Int = 0
    var finalAmount: Int = 0
    var interestEarned: Int = 0

    private val _history = MutableStateFlow<List<DepositEntity>>(emptyList())
    val history: StateFlow<List<DepositEntity>> = _history

    init {
        loadHistory()
    }

    fun calculateFinalAmount() {
        var amount = initialAmount.toDouble()
        for (i in 1..periodMonths) {
            val interest = amount * (interestRate / 100)
            amount += interest + monthlyTopUp
        }
        finalAmount = amount.roundToInt()
        interestEarned = finalAmount - initialAmount - monthlyTopUp * periodMonths
    }

    fun getCalculationSummary(): String {
        return """
            Стартовый взнос: $initialAmount
            Срок вклада: $periodMonths месяцев
            Процентная ставка: $interestRate%
            Ежемесячное пополнение: $monthlyTopUp
            Итоговая сумма: $finalAmount
            Начисленные проценты: $interestEarned
        """.trimIndent()
    }

    fun saveCalculation() {
        val deposit = DepositEntity(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        viewModelScope.launch {
            val currentHistory = repository.getHistory()

            val alreadyExists = currentHistory.any {
                it.initialAmount == deposit.initialAmount &&
                        it.periodMonths == deposit.periodMonths &&
                        it.interestRate == deposit.interestRate &&
                        it.monthlyTopUp == deposit.monthlyTopUp &&
                        it.finalAmount == deposit.finalAmount &&
                        it.interestEarned == deposit.interestEarned
            }

            if (!alreadyExists) {
                repository.insertDeposit(deposit)
                loadHistory()
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getHistory()
        }
    }

    fun deleteDeposit(
        initialAmount: Int,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Int,
        finalAmount: Int,
        interestEarned: Int
    ) {
        viewModelScope.launch {
            val currentHistory = repository.getHistory()

            val item = currentHistory.find {
                it.initialAmount == initialAmount &&
                        it.periodMonths == periodMonths &&
                        it.interestRate == interestRate &&
                        it.monthlyTopUp == monthlyTopUp &&
                        it.finalAmount == finalAmount &&
                        it.interestEarned == interestEarned
            }

            item?.let {
                repository.deleteDeposit(it)
                loadHistory()
            }
        }
    }
}