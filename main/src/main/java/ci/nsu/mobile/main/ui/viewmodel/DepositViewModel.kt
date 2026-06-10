package ci.nsu.mobile.main.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.token.TokenManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // Получаем ID текущего авторизованного пользователя для Лабы 7
    private val currentUserId: Long
        get() = tokenManager.getUserId()

    // Стрим с историей расчетов для новой вкладки в MainScreen
    val historyState: StateFlow<List<DepositCalculation>> = repository
        .getCalculationsForUser(currentUserId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- ПОЛЯ СОСТОЯНИЯ ДЛЯ СОВМЕСТИМОСТИ С СТАРАЫМ МАСТЕРОМ (Step1, Step2, Result) ---
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")
    var selectedRate by mutableStateOf(0.0)
    var availableRates by mutableStateOf<List<Double>>(listOf(5.0, 7.5, 10.0)) // Спиоск ставок для Step1/Step2
    var validationError by mutableStateOf<String?>(null)
    var calculationResult by mutableStateOf<Pair<Double, Double>?>(null)

    // --- МЕТОДЫ ДЛЯ Step1Screen.kt ---
    fun updateAvailableRates() {
        val months = periodMonths.toIntOrNull() ?: 0
        // Генерируем сетку процентов в зависимости от срока, чтобы оживить старый UI
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
        val amount = initialAmount.toDoubleOrNull()
        val months = periodMonths.toIntOrNull()
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

    // --- МЕТОДЫ ДЛЯ Step2Screen.kt ---
    fun validateStep2(): Boolean {
        validationError = null
        return true
    }

    fun calculate() {
        val startAmount = initialAmount.toDoubleOrNull() ?: 0.0
        val period = periodMonths.toIntOrNull() ?: 0
        val percent = selectedRate
        val monthly = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val interestEarned = startAmount * (percent / 100) * (period / 12.0)
        val finalAmount = startAmount + interestEarned + (monthly * period)
        calculationResult = Pair(finalAmount, interestEarned)
    }

    // --- ВАРИАНТ №1: Старый метод сохранения (для пошагового ResultScreen) ---
    fun saveCalculation(onSuccess: () -> Unit) {
        val amount = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val rate = selectedRate
        val topUp = monthlyTopUp.toDoubleOrNull()
        val res = calculationResult ?: Pair(0.0, 0.0)

        viewModelScope.launch {
            val calculation = DepositCalculation(
                userId = currentUserId, // Привязываем к текущему юзеру
                initialAmount = amount,
                periodMonths = months,
                interestRate = rate,
                monthlyTopUp = topUp,
                finalAmount = res.first,
                interestEarned = res.second,
                calculationDate = System.currentTimeMillis()
            )
            repository.saveCalculation(calculation)
            onSuccess()
        }
    }

    // --- ВАРИАНТ №2: Новый метод сохранения с параметрами (для инлайн-калькулятора в MainScreen) ---
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
                userId = currentUserId,
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

    // Метод сброса
    fun reset() {
        initialAmount = ""
        periodMonths = ""
        monthlyTopUp = ""
        selectedRate = 0.0
        validationError = null
        calculationResult = null
    }

    // Удаление расчета из базы
    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteCalculation(calculation)
        }
    }
}