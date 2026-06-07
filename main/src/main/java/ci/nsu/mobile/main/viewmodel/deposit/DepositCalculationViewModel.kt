package ci.nsu.mobile.main.viewmodel.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.room.DepositCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositCalculationViewModel @Inject constructor (
    val repository: DepositRepository,
    private val tokenManager: TokenManager
) : ViewModel(){
    private val _state = MutableStateFlow(DepositUIState())
    val state: StateFlow<DepositUIState> = _state.asStateFlow()

    fun depositCalculationEvent(event: DepositEvents) {
        when(event) {
            is DepositEvents.InitialAmountChanged -> {
                _state.update { it.copy(initialAmount = event.newInitialAmount,
                    errorFieldsFirstScreen = it.errorFieldsFirstScreen - "initialAmount") }
            }
            is DepositEvents.InterestRateChanged -> {
                _state.update { it.copy(interestRate = event.newInterestRate,
                    errorFieldsFirstScreen = it.errorFieldsFirstScreen - "interestRate") }
            }
            is DepositEvents.MonthlyTopUpChanged -> {
                _state.update { it.copy(monthlyTopUp = event.newMonthlyTopUp,
                    errorFieldsSecondScreen = it.errorFieldsSecondScreen - "monthlyTopUp") }
            }
            is DepositEvents.PeriodMonthsChanged -> {
                _state.update { it.copy(periodMonths = event.newPeriodMonths,
                    errorFieldsFirstScreen = it.errorFieldsFirstScreen - "periodMonths") }
            }
            is DepositEvents.IsMonthlyTopUpCheck -> {
                _state.update { it.copy(monthlyTopUpCheck = event.newCheck) }
            }
            is DepositEvents.SelectedRateUpdate -> {
                _state.update { it.copy(selectedInterestRate = event.newRate) }
            }
            is DepositEvents.CalculationFinalAmount -> calculateFinalAmount(event.initialAmount, event.interestRate,
                event.periodMonths, event.monthlyTopUp, event.date)
            is DepositEvents.CleanAll -> cleanAll()
            is DepositEvents.ValidationFirstScreen -> if (validationFirstScreen()) _state.update { it.copy(goToSecondScreen = true) }
            is DepositEvents.ValidationSecondScreen -> validationSecondScreen(event.isChecked)
            is DepositEvents.SaveEntity -> saveEntity()
            is DepositEvents.GoToResultScreen -> {
                _state.update { it.copy(goToResultScreen = event.value) }
            }
            is DepositEvents.GoToSecondScreen -> {
                _state.update { it.copy(goToSecondScreen = event.value) }

            }
        }
    }
    private fun cleanAll() {
        _state.update { DepositUIState() }
    }

    private fun validationFirstScreen(): Boolean {
        val state = _state.value
        val errorFields = mutableSetOf<String>()
        if (state.initialAmount.isEmpty() || state.initialAmount.toDoubleOrNull() == null ||
            state.initialAmount.toDouble() <= 0.0) {
            errorFields.add("initialAmount")
            _state.update { it.copy(goToSecondScreen = false) }
        }
        if (state.periodMonths.isEmpty() || state.periodMonths.toDoubleOrNull() == null ||
            state.periodMonths.toDouble() <= 0.0) {
            errorFields.add("periodMonths")
            _state.update { it.copy(goToSecondScreen = false) }
        }
        _state.update { it.copy( errorFieldsFirstScreen = errorFields) }
        if (errorFields.isEmpty()) {
            _state.update { it.copy(goToSecondScreen = true) }
        }
        return errorFields.isEmpty()
    }
    private fun validationSecondScreen(isChecked: Boolean) {
        val state = _state.value
        val errorFields = mutableSetOf<String>()
        if (state.interestRate.isEmpty() || state.interestRate.toIntOrNull() == null) {
            _state.update { it.copy(goToResultScreen = false) }
            errorFields.add("interestRate")
        }
        if (isChecked) {
            val topUpValue = state.monthlyTopUp?.toDoubleOrNull()
            if (topUpValue == null || topUpValue <= 0.0) {
                _state.update { it.copy(goToResultScreen = false) }
                errorFields.add("monthlyTopUp")
            }
        }
        _state.update { it.copy( errorFieldsSecondScreen = errorFields) }
        if (errorFields.isEmpty()) {
            _state.update { it.copy(goToResultScreen = true) }
        }
    }

    private fun calculateFinalAmount(initialAmount: Double, interestRate: Int, periodMonths: Int, monthlyTopUp: Double?,
                                     date: Long
    ) {
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
        _state.update { it.copy(finalAmount = finalAmount, interestEarned = totalInterest, calculationDate = date) }
    }

    private fun saveEntity() {
        viewModelScope.launch {
            val state = _state.value
            val entity = DepositCalculation(
                userId = tokenManager.userId.toLong(),
                initialAmount = state.initialAmount.toDouble(),
                periodMonths = state.periodMonths.toInt(),
                interestRate = state.interestRate.toInt(),
                monthlyTopUp = state.monthlyTopUp?.toDoubleOrNull(),
                finalAmount = state.finalAmount,
                interestEarned = state.interestEarned,
                calculationDate = state.calculationDate
            )
            repository.insertDeposit(entity)
        }
    }
}