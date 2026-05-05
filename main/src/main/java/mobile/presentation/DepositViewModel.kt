package mobile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobile.data.DepositCalculation
import mobile.domain.DepositRepository

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {


    // ==========================================
    // ЭТАП 1: Ввод основных параметров
    // ==========================================
    var initialAmount: Double = 0.0
    var periodMonths: Int = 0

    // ==========================================
    // ЭТАП 2: Дополнительные параметры
    // ==========================================
    var interestRate: Double = 0.0
    var monthlyTopUp: Double? = null // null, если юзер ничего не ввел

    // ==========================================
    // РЕЗУЛЬТАТ И ИСТОРИЯ
    // ==========================================

    // StateFlow хранит текущий результат расчета для экрана результатов
    private val _calculationResult = MutableStateFlow<DepositCalculation?>(null)
    val calculationResult: StateFlow<DepositCalculation?> = _calculationResult.asStateFlow()
    val historyFlow = repository.allCalculations
    // История всех расчетов напрямую из БД
    val allCalculations = repository.allCalculations

    // ==========================================
    // ЛОГИКА И ПРАВИЛА (Бизнес-логика)
    // ==========================================

    // Определение доступной ставки в зависимости от срока
// Функция выдает список ставок в зависимости от срока вклада
    fun getAvailableRates(): List<Double> {
        val rates = mutableListOf<Double>()

        // Изначально 15% доступна всегда, как ты и сказал
        rates.add(15.0)

        // Проверяем срок (periodMonths мы сохранили еще на Этапе 1)
        if (periodMonths > 6) {
            // Если больше 6 месяцев, добавляем в выбор 10%
            rates.add(10.0)
        }

        // Можешь легко добавлять свои условия дальше. Например:
        // if (periodMonths >= 12) {
        //     rates.add(8.5)
        // }

        return rates
    }

    // Выполнение математического расчета (Сложный процент с пополнением)
    fun calculate() {
        var currentBalance = initialAmount
        var totalInvested = initialAmount
        val monthlyRate = interestRate / 100.0 / 12.0

        for (i in 1..periodMonths) {
            // 1. Начисляем процент на остаток за месяц
            currentBalance += currentBalance * monthlyRate

            // 2. Добавляем ежемесячное пополнение (если оно есть)
            val topUp = monthlyTopUp ?: 0.0
            currentBalance += topUp
            totalInvested += topUp
        }

        val finalAmount = currentBalance
        val interestEarned = finalAmount - totalInvested

        // Формируем DBO объект для сохранения
        val result = DepositCalculation(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis() // Текущее время
        )

        // Публикуем результат, чтобы UI его увидел
        _calculationResult.value = result
    }

    // Сохранение в базу данных Room
    fun saveCalculation() {
        _calculationResult.value?.let { calc ->
            viewModelScope.launch {
                repository.insert(calc)
            }
        }
    }

    // Очистка данных (для кнопки "В начало")
    fun clearData() {
        initialAmount = 0.0
        periodMonths = 0
        interestRate = 0.0
        monthlyTopUp = null
        _calculationResult.value = null
    }
}