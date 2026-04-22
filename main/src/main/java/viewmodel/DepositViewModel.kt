package viewmodel

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import data.DepositCalculations
import data.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Month
import java.time.temporal.TemporalAmount

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _termInMonths = MutableStateFlow("") //срок в месяцах
    private val _interestRate = MutableStateFlow(0.0) //ставка
    private val _monthlyDeposit = MutableStateFlow("") //ежемесячный платеж
    private val _selectedCurrency = MutableStateFlow("Рубли") //выбранная валюта
    private val _initialAmount = MutableStateFlow("") //начальная сумма
    private val _totalAmount = MutableStateFlow(0.0) //итоговая сумма
    private val _accruedInterest = MutableStateFlow(0.0) //начисленные проценты
    private val _errorMessage = MutableStateFlow<String?>(null) //ошибка


    val termInMonths: StateFlow<String> = _termInMonths.asStateFlow()
    val interestRate: StateFlow<Double> = _interestRate.asStateFlow()
    val monthlyDeposit: StateFlow<String> = _monthlyDeposit.asStateFlow()
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()
    val initialAmount: StateFlow<String> = _initialAmount.asStateFlow()
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()
    val accruedInterest: StateFlow<Double> = _accruedInterest.asStateFlow()
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    //обновление начальной суммы
    fun updateInititalAmount(value: String) {
        _initialAmount.value = value
    }
    //обновление срока в месяцах
    fun updateTermInMonths(value: String) {
        _termInMonths.value = value
        val months = value.toIntOrNull() ?: 0
        _interestRate.value = calculateInterestRate(months)
    }
    //обновление ежемесячного платежа
    fun updateMonthlyDeposit(value: String) {
        _monthlyDeposit.value = value
    }
    //обновление выбранной валюты
    fun updateSelectedCurrency(value: String) {
        _selectedCurrency.value = value
    }

    //расчет процентной ставки по сроку
    private fun calculateInterestRate(months: Int): Double {
        return when {
            months < 6-> 15.0
            months < 12 -> 10.0
            else -> 5.0
        }
    }

    //расчет итоговой суммы
    private fun calculateTotalAmount(amount: Double, term: Int, deposit: Double) :
            Pair<Double, Double> //возврат 2-х значений
    {
        val rate = calculateInterestRate(term)
        val monthlyRate = rate / 12.0 / 100.0
        var currentAmount = amount
        for (n in 1..term)
        {
            currentAmount += (currentAmount * monthlyRate)
            currentAmount += deposit
        }
        val totalDeposited = amount + (deposit * term)
        val interestEarned = currentAmount - totalDeposited
        return Pair(currentAmount, interestEarned)
    }


    //расчет
    public fun performCalculation()
    {
        //начальная сумма проверки
        if (_initialAmount.value.isEmpty())
        {
            _errorMessage.value = "Начальное значение пустое"
            return
        }
        val countInitialAmount = _initialAmount.value.toDoubleOrNull()
        if (countInitialAmount == null)
        {
            _errorMessage.value = "Ошибка начального значения"
            return
        }
        if (countInitialAmount <= 0)
        {
            _errorMessage.value = "Начальное значение от 1"
            return
        }

        //срок проверки
        if (_termInMonths.value == "")
        {
            _errorMessage.value = "Срок не может быть пустым"
            return
        }
        val termMonthly = _termInMonths.value.toIntOrNull()
        if (termMonthly == null || termMonthly < 1)
        {
            _errorMessage.value = "Пополнение в месяц больше 1"
            return
        }

        //пополнение в месяц проверки
        if (_monthlyDeposit.value.isEmpty())
        {
            _monthlyDeposit.value = "0.0"
        }
        val monthDeposit = _monthlyDeposit.value.toDoubleOrNull()
        if (monthDeposit == null || monthDeposit < 0)
        {
            _errorMessage.value = "Пополнение не может быть отрицательным"
            return
        }


        val result = calculateTotalAmount(countInitialAmount, termMonthly, monthDeposit)

        _totalAmount.value = result.first
        _accruedInterest.value = result.second
        _errorMessage.value = null
    }


    //загрузка истории
    fun getAllHistory(): Flow<List<DepositCalculations>> {
        return repository.getAllCalculations()
    }

    //TODO:сохранение расчета
    fun saveCalculation()
    {

    }


}