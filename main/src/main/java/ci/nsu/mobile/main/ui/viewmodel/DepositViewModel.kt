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
import java.util.Locale

class DepositViewModel(
    private val repo: DepositRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    //  Приватный поток для ID пользователя
    private val _userId = MutableStateFlow(authRepo.getUserId() ?: 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val calculations: StateFlow<List<DepositCalculation>> = _userId
        .flatMapLatest { uid ->
            if (uid > 0) repo.getCalculations(uid) else flowOf(emptyList())
        }
        // 🟢 WhileSubscribed(5000) лучше Lazily: отключает поток БД, когда экран не виден, экономя ресурсы
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _calcResult = MutableStateFlow<DepositCalculation?>(null)
    val calcResult: StateFlow<DepositCalculation?> = _calcResult.asStateFlow()

    // 🟢 Вызывать при успешном логине или выходе
    fun refreshUserId() {
        _userId.value = authRepo.getUserId() ?: 0L
        _calcResult.value = null // Сбрасываем результат при смене пользователя
    }

    fun calculateDeposit(amount: Double, months: Int, rate: Double, topUp: Double) {
        // 🟢 1. Жёсткая валидация входных данных
        if (amount <= 0 || months <= 0 || rate < 0 || topUp < 0) {
            _calcResult.value = null
            return
        }

        val uid = _userId.value
        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100.0 / 12.0

        // 🟢 2. Математически верный цикл ежемесячной капитализации
        for (i in 1..months) {
            val interest = total * monthlyRate
            earned += interest
            total += interest + topUp
        }

        // 🟢 3. Финансовое округление до 2 знаков (избегаем 1000.0000000001)
        val finalAmountRounded = String.format(Locale.US, "%.2f", total).toDouble()
        val earnedRounded = String.format(Locale.US, "%.2f", earned).toDouble()

        val calculation = DepositCalculation(
            userId = uid,
            initialAmount = amount,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = finalAmountRounded,
            interestEarned = earnedRounded,
            calculationDate = System.currentTimeMillis()
        )
        _calcResult.value = calculation
    }

    fun saveCalculation(calc: DepositCalculation) {
        viewModelScope.launch {
            // 🟢 4. Гарантируем, что сохранится с актуальным ID, даже если пользователь сменился во время расчёта
            val safeCalc = calc.copy(userId = _userId.value)
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