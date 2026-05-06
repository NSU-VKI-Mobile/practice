package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.DepositCalculations
import data.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
        _errorMessage.value = null
    }
    //обновление срока в месяцах
    fun updateTermInMonths(value: String) {
        _termInMonths.value = value
        val months = value.toIntOrNull() ?: 0
        _interestRate.value = calculateInterestRate(months)
        _errorMessage.value = null
    }
    //обновление ежемесячного платежа
    fun updateMonthlyDeposit(value: String) {
        _monthlyDeposit.value = value
        _errorMessage.value = null
    }
    //обновление выбранной валюты
    fun updateSelectedCurrency(value: String) {
        _selectedCurrency.value = value
    }
    fun clearError() {
        _errorMessage.value = null
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
    fun performCalculation(): Boolean {
        if (_initialAmount.value.isEmpty()) {
            _errorMessage.value = "Введите начальную сумму"
            return false
        }
        val amount = _initialAmount.value.toDoubleOrNull()
        if (amount == null) {
            _errorMessage.value = "Стартовый взнос должен быть числом"
            return false
        }
        if (amount <= 0) {
            _errorMessage.value = "Стартовый взнос должен быть больше нуля"
            return false
        }

        if (_termInMonths.value.isEmpty()) {
            _errorMessage.value = "Введите срок вклада"
            return false
        }
        val term = _termInMonths.value.toIntOrNull()
        if (term == null) {
            _errorMessage.value = "Срок вклада должен быть целым числом"
            return false
        }
        if (term < 1) {
            _errorMessage.value = "Срок вклада должен быть не менее 1 месяца"
            return false
        }

        val deposit = if (_monthlyDeposit.value.isEmpty()) {
            0.0
        } else {
            _monthlyDeposit.value.toDoubleOrNull() ?: 0.0
        }
        if (deposit < 0) {
            _errorMessage.value = "Пополнение не может быть отрицательным"
            return false
        }

        val result = calculateTotalAmount(amount, term, deposit)
        _totalAmount.value = result.first
        _accruedInterest.value = result.second
        _errorMessage.value = null
        return true
    }


    //загрузка истории
    fun getAllHistory(): Flow<List<DepositCalculations>> {
        return repository.getAllCalculations()
    }

    //сохранение расчета
    fun saveCalculation()
    {
            viewModelScope.launch { //запуск корутины - выполнение в фоне, экраны не заморожены

                try {
                    if (_totalAmount.value > 0) {
                        val objectDeposit = DepositCalculations(
                            id = 0,
                            initialAmount = _initialAmount.value.toDouble(),
                            termMonths = _termInMonths.value.toInt(),
                            interestRate = _interestRate.value,
                            monthlyTopUp = _monthlyDeposit.value.toDoubleOrNull(),
                            finalAmount = _totalAmount.value,
                            interestEarned = _accruedInterest.value,
                            calculationDate = System.currentTimeMillis(),
                            currency = _selectedCurrency.value
                        )
                        repository.saveCalculation(objectDeposit)
                    } else {
                        _errorMessage.value = "Сначала выполните расчет"
                    }
                }
                catch (e: Exception)
                {
                    _errorMessage.value = "Ошибка сохранения: ${e.message}"
                }
            }
    }

    suspend fun getCalculationById(id: Long): DepositCalculations? {
        return repository.getCalculationById(id)
    }
}

