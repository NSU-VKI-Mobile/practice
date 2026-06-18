package ci.nsu.mobile.main.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.token.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // наблюдаемый userId (обновляется при логине/выходе)
    private val userIdFlow = MutableStateFlow(tokenManager.getUserId())

    // подписываемся на изменения userId и при каждом новом userId запрашиваем его вклады
    val historyState: StateFlow<List<DepositCalculation>> = userIdFlow
        .flatMapLatest { userId ->
            if (userId == -1L) {
                MutableStateFlow(emptyList())
            } else {
                repository.getCalculationsForUser(userId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")
    var selectedRate by mutableStateOf(0.0)
    var availableRates by mutableStateOf<List<Double>>(listOf(5.0, 7.5, 10.0))
    var validationError by mutableStateOf<String?>(null)
    var calculationResult by mutableStateOf<Pair<Double, Double>?>(null)

    fun updateAvailableRates() {
        val months = periodMonths.toIntOrNull() ?: 0
        calculateRates(months)
    }

    fun updateAvailableRates(monthsStr: String) {
        val months = monthsStr.toIntOrNull() ?: 0
        calculateRates(months)
    }

    fun updateAvailableRates(months: Int) {
        calculateRates(months)
    }

    private fun calculateRates(months: Int) {
        availableRates = when {
            months <= 3 -> listOf(4.5, 5.0, 5.5)
            months <= 6 -> listOf(6.0, 6.5, 7.0)
            months <= 12 -> listOf(8.0, 8.5, 9.0)
            else -> listOf(11.0, 12.0, 14.0)
        }
        if (availableRates.isNotEmpty()) {
            selectedRate = availableRates.first()
        }
    }

    fun validateStep1(): Boolean {
        return validateStep1Fields(initialAmount, periodMonths)
    }

    fun validateStep1(amount: String, months: String): Boolean {
        return validateStep1Fields(amount, months)
    }

    private fun validateStep1Fields(amountStr: String, monthsStr: String): Boolean {
        val amount = amountStr.toDoubleOrNull()
        val months = monthsStr.toIntOrNull()
        if (amount == null || amount <= 0) {
            validationError = "Введите корректную сумму вклада"
            return false
        }
        if (months == null || months <= 0) {
            validationError = "Введите корректный срок вклада"
            return false
        }
        validationError = null
        return true
    }

    fun validateStep2(): Boolean {
        return if (selectedRate <= 0.0) {
            validationError = "Выберите процентную ставку"
            false
        } else {
            validationError = null
            true
        }
    }

    fun validateStep2(any: Any?): Boolean = validateStep2()

    fun calculate() {
        val startAmount = initialAmount.toDoubleOrNull() ?: 0.0
        val period = periodMonths.toIntOrNull() ?: 0
        val percent = selectedRate
        val monthly = monthlyTopUp.toDoubleOrNull() ?: 0.0
        runCalculationFormula(startAmount, period, percent, monthly)
    }

    fun calculate(amount: String, months: String, rate: Double, topUp: String) {
        val startAmount = amount.toDoubleOrNull() ?: 0.0
        val period = months.toIntOrNull() ?: 0
        val monthly = topUp.toDoubleOrNull() ?: 0.0
        runCalculationFormula(startAmount, period, rate, monthly)
    }

    private fun runCalculationFormula(startAmount: Double, period: Int, percent: Double, monthly: Double) {
        val interestEarned = startAmount * (percent / 100) * (period / 12.0)
        val finalAmount = startAmount + interestEarned + (monthly * period)
        calculationResult = Pair(finalAmount, interestEarned)
    }

    fun saveCalculation(onSuccess: () -> Unit) {
        val amount = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val rate = selectedRate
        val topUp = monthlyTopUp.toDoubleOrNull()
        val res = calculationResult ?: Pair(0.0, 0.0)

        viewModelScope.launch {
            val calculation = DepositCalculation(
                userId = tokenManager.getUserId(),
                initialAmount = amount,
                periodMonths = months,
                interestRate = rate,
                monthlyTopUp = topUp,
                finalAmount = res.first,
                interestEarned = res.second,
                calculationDate = System.currentTimeMillis()
            )
            repository.saveCalculation(calculation)
            reset()
            onSuccess()
        }

        val currentUserId = tokenManager.getUserId()
        Log.d("DepositVM", "Saving with userId=$currentUserId")
    }

    fun saveCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?,
        finalAmount: Double,
        interestEarned: Double
    ) {
        viewModelScope.launch {
            val calculation = DepositCalculation(
                userId = tokenManager.getUserId(),
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                calculationDate = System.currentTimeMillis()
            )
            repository.saveCalculation(calculation)
        }
    }

    fun reset() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = 0.0
        validationError = null
        calculationResult = null
    }

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
        }
    }

    // Вызывать после логина или выхода
    fun refreshUserId() {
        userIdFlow.value = tokenManager.getUserId()
    }
}