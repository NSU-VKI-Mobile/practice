package ci.nsu.mobile.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    private val _initialAmount = mutableStateOf(0.0)
    private val _periodMonths = mutableStateOf(0)
    private val _interestRate = mutableStateOf(0.0)
    private val _monthlyTopUp = mutableStateOf(0.0)
    private val _finalAmount = mutableStateOf(0.0)
    private val _interestEarned = mutableStateOf(0.0)

    // Флаг, чтобы нельзя было сохранить дважды
    private var lastSavedId = 0L

    val initialAmount: Double get() = _initialAmount.value
    val periodMonths: Int get() = _periodMonths.value
    val interestRate: Double get() = _interestRate.value
    val monthlyTopUp: Double get() = _monthlyTopUp.value
    val finalAmount: Double get() = _finalAmount.value
    val interestEarned: Double get() = _interestEarned.value

    fun calculate(initialAmount: Double, periodMonths: Int, interestRate: Double, monthlyTopUp: Double) {
        val monthlyRate = interestRate / 100 / 12
        var currentAmount = initialAmount

        for (month in 1..periodMonths) {
            currentAmount += monthlyTopUp
            currentAmount += currentAmount * monthlyRate
        }

        _initialAmount.value = initialAmount
        _periodMonths.value = periodMonths
        _interestRate.value = interestRate
        _monthlyTopUp.value = monthlyTopUp
        _finalAmount.value = currentAmount
        _interestEarned.value = currentAmount - initialAmount - (monthlyTopUp * periodMonths)
    }

    fun getDepositEntity(): DepositEntity {
        return DepositEntity(
            id = 0,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )
    }

    // Проверка, сохраняли ли уже этот расчёт
    fun canSave(): Boolean {
        val currentId = "${initialAmount}_${periodMonths}_${interestRate}_${monthlyTopUp}".hashCode().toLong()
        return if (currentId == lastSavedId) {
            false  // Уже сохраняли
        } else {
            lastSavedId = currentId
            true
        }
    }
}