package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.DepositCalculation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculationViewModel(
    private val initialAmount: Double,
    private val periodMonths: Int
) : ViewModel() {

    // Доступные ставки
    private val _availableRates = MutableStateFlow(listOf(5.0, 10.0, 15.0))
    val availableRates: StateFlow<List<Double>> = _availableRates.asStateFlow()

    // Выбранная ставка
    private val _selectedRate = MutableStateFlow(5.0)
    val selectedRate: StateFlow<Double> = _selectedRate.asStateFlow()

    // Ежемесячное пополнение
    private val _monthlyTopUp = MutableStateFlow("")
    val monthlyTopUp: StateFlow<String> = _monthlyTopUp.asStateFlow()

    // Ошибки
    private val _rateError = MutableStateFlow<String?>(null)
    val rateError: StateFlow<String?> = _rateError.asStateFlow()

    private val _periodError = MutableStateFlow<String?>(null)
    val periodError: StateFlow<String?> = _periodError.asStateFlow()

    // ТЕКУЩИЙ (обновляемый) срок вклада — начинается с исходного периода
    private val _currentPeriodMonths = MutableStateFlow(periodMonths)
    val currentPeriodMonths: StateFlow<Int> = _currentPeriodMonths.asStateFlow()

    // Получить минимальный срок для выбранной ставки
    private fun getMinPeriodForRate(rate: Double): Int {
        return when (rate) {
            5.0 -> 3
            10.0 -> 6
            15.0 -> 12
            else -> 3
        }
    }

    // Обновить срок вклада в соответствии с выбранной ставкой
    private fun updatePeriodByRate(rate: Double) {
        val newPeriod = getMinPeriodForRate(rate)
        _currentPeriodMonths.value = newPeriod
        validatePeriod()
    }

    // Проверка срока (на случай если пользователь как-то ещё меняет период)
    private fun validatePeriod() {
        val currentPeriod = _currentPeriodMonths.value
        val rate = _selectedRate.value
        val minPeriod = getMinPeriodForRate(rate)

        if (currentPeriod < minPeriod) {
            _periodError.value = "Для ставки ${rate}% минимальный срок — $minPeriod месяцев"
        } else {
            _periodError.value = null
        }
    }

    // Обновить выбранную ставку (и автоматически — срок)
    fun updateSelectedRate(rate: Double) {
        _selectedRate.value = rate
        updatePeriodByRate(rate)   // ← вот здесь меняется срок!
    }

    // Обновить ежемесячное пополнение
    fun updateMonthlyTopUp(value: String) {
        _monthlyTopUp.value = value
    }

    // Рассчитать вклад с ИСПРАВЛЕННЫМ сроком
    fun calculateDeposit(): DepositCalculation? {
        val rate = _selectedRate.value
        val topUp = _monthlyTopUp.value.toDoubleOrNull() ?: 0.0
        val currentPeriod = _currentPeriodMonths.value   // ← обновлённый срок

        if (periodError.value != null) return null

        // Простейший расчёт (без капитализации — только для примера)
        val monthlyRate = rate / 100 / 12
        var total = initialAmount
        for (i in 1..currentPeriod) {
            total += total * monthlyRate
            total += topUp
        }
        val finalAmount = total
        val interestEarned = finalAmount - initialAmount - topUp * currentPeriod

        return DepositCalculation(
            initialAmount = initialAmount,
            periodMonths = currentPeriod,        // ← здесь ПРАВИЛЬНЫЙ срок
            interestRate = rate,
            monthlyTopUp = if (topUp > 0) topUp else null,
            interestEarned = interestEarned,
            finalAmount = finalAmount
        )
    }
}