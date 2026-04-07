package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculationEntity
import ci.nsu.mobile.main.data.model.DepositUIState
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositCalculationViewModel(val repos: DepositRepository): ViewModel() {
    private val _uiState = MutableStateFlow(DepositUIState())
    val uiState: StateFlow<DepositUIState> = _uiState.asStateFlow()
    private val _historyState = MutableStateFlow<List<DepositCalculationEntity>>(emptyList())
    val historyState: StateFlow<List<DepositCalculationEntity>> = _historyState.asStateFlow()

    private val _selectedDeposit = MutableStateFlow<DepositCalculationEntity?>(null)
    val selectedDeposit: StateFlow<DepositCalculationEntity?> = _selectedDeposit.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            repos.GetAll().collect { deposits -> _historyState.value = deposits }
        }
    }
    fun loadDepositById(id: Long) {
        viewModelScope.launch {
            repos.GetById(id).collect { deposit -> _selectedDeposit.value = deposit }
        }
    }
    fun validationFirstScreen(): String {
        val state = _uiState.value
        if (state.initialAmount.isEmpty()) {
            return "Введите стартовый взнос!"
        }
        if (state.initialAmount.toDoubleOrNull() == null) {
            return "Стартовый взнос должен быть числом"
        }
        if (state.periodMonths.isEmpty()) {
            return "Введите срок вклада в месяцах"
        }
        if (state.periodMonths.toDoubleOrNull() == null) {
            return "Срок вклада должен быть целым числом"
        }
        return ""
    }

    fun validationSecondScreen(isChecked: Boolean): String {
        val state = _uiState.value
        if (isChecked) {
            if (state.interestRate.isEmpty()) {
                return "Выберете процентную ставку"
            }
            if (state.interestRate.toIntOrNull() == null) {
                return "Выберете процентную ставку"
            }
            if (state.monthlyTopUp?.toDoubleOrNull() == null) {
                return "Укажите сумму ежемесячного пополнения"
            }
        }
        else {
            if (state.interestRate.isEmpty()) {
                return "Выберете процентную ставку"
            }
            if (state.interestRate.toIntOrNull() == null) {
                return "Выберете процентную ставку"
            }
        }
        return ""
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

    fun calculateFinalAmount(initialAmount: Double, interestRate: Int, periodMonths: Int, monthlyTopUp: Double?
    ): Pair<Double, Double> {
        val monthlyRate = interestRate / 100 / 12
        var finalAmountC = initialAmount
        var totalInterestC = 0.0

        for (month in 1..periodMonths) {
            val monthlyInterest = finalAmountC * monthlyRate
            totalInterestC += monthlyInterest
            finalAmountC += monthlyInterest

            monthlyTopUp?.let { topUp ->
                if (topUp > 0) {
                    finalAmountC += topUp
                }
            }
        }
        return Pair(finalAmountC, totalInterestC)
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
    fun cleanAll() {
        _uiState.update { DepositUIState() }
    }

    suspend fun saveEntity() {
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
        repos.insertDeposit(entity)
    }

}
