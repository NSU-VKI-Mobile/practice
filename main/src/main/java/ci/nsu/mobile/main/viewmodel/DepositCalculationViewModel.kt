package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.DepositCalculationEntity
import ci.nsu.mobile.main.domain.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DepositCalculationViewModel(val repos: DepositRepository): ViewModel() {
    private val _uiState = MutableStateFlow(DepositUIState())
    val uiState: StateFlow<DepositUIState> = _uiState.asStateFlow()
    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    fun cleanAll() {
        _uiState.update { DepositUIState() }
    }

    fun initialAmountUpdate(newValue: String) {
        _uiState.update { it.copy(initialAmount = newValue) }
    }
    fun periodMonthUpdate(newValue: String) {
        _uiState.update { it.copy(periodMonths = newValue) }
    }
    fun monthlyTopUpUpdate(newValue: String) {
        _uiState.update { it.copy(monthlyTopUp = newValue) }
    }
    fun interestRateUpdate(newValue: String) {
        _uiState.update { it.copy(interestRate = newValue) }
    }
    fun updateHasMonthlyTopUp(hasTopUp: Boolean) {
        _uiState.update { it.copy(monthlyTopUpCheck = hasTopUp) }
    }
    fun updateSelectedRate(rate: Int) {
        _uiState.update { it.copy(selectedInterestRate = rate) }
    }
    fun updateCalculationResult(finalAmount: Double, interestEarned: Double, date: Long) {
        _uiState.update {
            it.copy(
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                calculationDate = date
            )
        }
    }
    fun validationFirstScreen(): Boolean {
        val state = _uiState.value
        if (state.initialAmount.isEmpty()) {
            _errorMessage.value = "Введите стартовый взнос!"
            return false
        }
        if (state.initialAmount.toDoubleOrNull() == null) {
            _errorMessage.value = "Стартовый взнос должен быть числом"
            return false
        }
        if (state.initialAmount.toDouble() <= 0.0) {
            _errorMessage.value = "Стартовый взнос должен быть больше 0"
            return false
        }
        if (state.periodMonths.isEmpty()) {
            _errorMessage.value = "Введите срок вклада в месяцах"
            return false
        }
        if (state.periodMonths.toDoubleOrNull() == null) {
            _errorMessage.value = "Срок вклада должен быть целым числом"
            return false
        }
        if (state.periodMonths.toDouble() <= 0.0) {
            _errorMessage.value = "Срок вклада должен быть больше 0"
            return false
        }
        return true
    }
    fun validationSecondScreen(isChecked: Boolean): Boolean {
        val state = _uiState.value
        if (state.interestRate.isEmpty() || state.interestRate.toIntOrNull() == null) {
            _errorMessage.value = "Выберите процентную ставку"
            return false
        }

        if (isChecked) {
            val topUpValue = state.monthlyTopUp?.toDoubleOrNull()
            if (topUpValue == null) {
                _errorMessage.value = "Укажите корректную сумму пополнения"
                return false
            }
            if (topUpValue <= 0.0) {
                _errorMessage.value = "Сумма пополнения должна быть больше 0"
                return false
            }
        }
        return true
    }

    fun calculateFinalAmount(initialAmount: Double, interestRate: Int, periodMonths: Int, monthlyTopUp: Double?
    ): Pair<Double, Double> {
        val monthlyRate = interestRate / 100.0 / 12.0
        var finalAmount = initialAmount
        for (month in 1..periodMonths) {
            finalAmount += finalAmount * monthlyRate

            monthlyTopUp?.let { topUp ->
                finalAmount += topUp
            }
        }
        val totalDeposited = initialAmount + (monthlyTopUp ?: 0.0) * periodMonths
        val totalInterest = finalAmount - totalDeposited
        return Pair(finalAmount, totalInterest)
    }
    suspend fun saveEntity(): Boolean {
        val state = _uiState.value
        val entity = DepositCalculationEntity(
            initialAmount = state.initialAmount.toDouble(),
            periodMonths = state.periodMonths.toInt(),
            interestRate = state.interestRate.toInt(),
            monthlyTopUp = state.monthlyTopUp?.toDoubleOrNull(),
            finalAmount = state.finalAmount,
            interestEarned = state.interestEarned,
            calculationDate = state.calculationDate
        )
        val exist = repos.findDuplication(entity)
        if (exist == null) {
            repos.insertDeposit(entity)
             return true
        } else {
            _errorMessage.value = "Расчет уже сохранен!"
            return false
        }
    }
}