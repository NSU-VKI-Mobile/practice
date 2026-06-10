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

    // --- ВОЗВРАЩАЕМ ПОЛЯ СОСТОЯНИЯ ДЛЯ СОВМЕСТИМОСТИ С ResultScreen.kt ---
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")
    var selectedRate by mutableStateOf(0.0)
    var validationError by mutableStateOf<String?>(null)
    var calculationResult by mutableStateOf<Pair<Double, Double>?>(null)

    // --- ВАРИАНТ №1: Старый метод сохранения (вызывается из ResultScreen с лямбдой onSuccess) ---
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

    // --- ВАРИАНТ №2: Новый метод сохранения с параметрами (вызывается из MainScreen) ---
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