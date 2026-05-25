package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repo: DepositRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    // 🟢 Реактивный источник текущего userId
    private val currentUserId = MutableStateFlow(authRepo.getUserId() ?: 0L)

    // 🟢 Динамический поток расчетов: автоматически переключается при смене userId
    @OptIn(ExperimentalCoroutinesApi::class)
    val calculations: StateFlow<List<DepositCalculation>> = currentUserId
        .flatMapLatest { userId ->
            if (userId > 0) repo.getCalculations(userId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _calcResult = MutableStateFlow<DepositCalculation?>(null)
    val calcResult: StateFlow<DepositCalculation?> = _calcResult.asStateFlow()

    // 🟢 Метод для обновления userId при входе/выходе
    fun refreshUserId() {
        currentUserId.value = authRepo.getUserId() ?: 0L
        _calcResult.value = null // Сбрасываем временный результат расчета
    }

    fun calculateDeposit(amount: Double, months: Int, rate: Double, topUp: Double) {
        val uid = currentUserId.value
        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            val interest = total * monthlyRate
            earned += interest
            total += interest + topUp
        }

        val calculation = DepositCalculation(
            userId = uid, // 🟢 Берем актуальный ID из потока
            initialAmount = amount,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = total,
            interestEarned = earned,
            calculationDate = System.currentTimeMillis()
        )
        _calcResult.value = calculation
    }

    fun saveCalculation(calc: DepositCalculation) {
        viewModelScope.launch {
            // Гарантируем, что сохраняем с актуальным userId
            val safeCalc = calc.copy(userId = currentUserId.value)
            repo.saveCalculation(safeCalc)
            _calcResult.value = null
        }
    }

    fun deleteCalculation(calc: DepositCalculation) {
        viewModelScope.launch {
            repo.deleteCalculation(calc)
        }
    }
}