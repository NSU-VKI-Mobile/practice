package ci.nsu.mobile.main.viewmodel.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.room.DepositCalculationEntity
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
                _state.update { it.copy(periodMonths = event.newPeriodMonth,
                    errorFieldsFirstScreen = it.errorFieldsFirstScreen - "periodMonth") }
            }
            is DepositEvents.IsMonthlyTopUpCheck -> {
                _state.update { it.copy(monthlyTopUpCheck = event.newCheck) }
            }
            is DepositEvents.SelectedRateUpdate -> {
                _state.update { it.copy(selectedInterestRate = event.newRate) }
            }
            is DepositEvents.UpdateCalculationResult -> {
                _state.update {
                    it.copy(
                        finalAmount = event.finalAmount,
                        interestEarned = event.interestEarned,
                        calculationDate = event.date
                    )
                }
            }
            is DepositEvents.CalculationFinalAmount -> calculateFinalAmount(event.initialAmount, event.interestRate,
                event.periodMonths, event.monthlyTopUp)
            is DepositEvents.CleanAll -> cleanAll()
            is DepositEvents.ValidationFirstScreen -> validationFirstScreen()
            is DepositEvents.ValidationSecondScreen -> validationSecondScreen(event.isChecked)
            is DepositEvents.SaveEntity -> saveEntity()
        }
    }
    private fun cleanAll() {
        _state.update { DepositUIState() }
    }

    private fun validationFirstScreen() {
        val state = _state.value
        val errorFields = mutableSetOf<String>()
        if (state.initialAmount.isEmpty() || state.initialAmount.toDoubleOrNull() == null ||
            state.initialAmount.toDouble() <= 0.0) errorFields.add("initialAmount")
        if (state.periodMonths.isEmpty() || state.periodMonths.toDoubleOrNull() == null ||
            state.periodMonths.toDouble() <= 0.0) errorFields.add("periodMonth")
        _state.update { it.copy( errorFieldsFirstScreen = errorFields) }
        if (errorFields.isEmpty()) {
            _state.update { it.copy(goToSecondScreen = true) }
        }
    }
    private fun validationSecondScreen(isChecked: Boolean) {
        val state = _state.value
        val errorFields = mutableSetOf<String>()
        if (state.interestRate.isEmpty() || state.interestRate.toIntOrNull() == null)
            errorFields.add("interestRate")

        if (isChecked) {
            val topUpValue = state.monthlyTopUp?.toDoubleOrNull()
            if (topUpValue == null || topUpValue <= 0.0) errorFields.add("monthlyTopUp")
        }
        _state.update { it.copy( errorFieldsSecondScreen = errorFields) }
        if (errorFields.isEmpty()) {
            _state.update { it.copy(goToResultScreen = true) }
        }
    }

    private fun calculateFinalAmount(initialAmount: Double, interestRate: Int, periodMonths: Int, monthlyTopUp: Double?
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

    private fun saveEntity() {
        viewModelScope.launch {
            val state = _state.value
            val entity = DepositCalculationEntity(
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