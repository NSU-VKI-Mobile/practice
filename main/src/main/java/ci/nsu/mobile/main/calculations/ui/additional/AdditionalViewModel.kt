package ci.nsu.mobile.main.calculations.ui.additional

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.calculations.domain.model.RateRule
import ci.nsu.mobile.main.calculations.domain.model.RateRules
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AdditionalViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdditionalUiState())
    val uiState: StateFlow<AdditionalUiState> = _uiState.asStateFlow()

    init {
        // Get arguments from navigation
        val amount = savedStateHandle.get<String>("amount") ?: ""
        val term = savedStateHandle.get<String>("term") ?: ""

        val isAmountValid = amount.toDoubleOrNull() != null && amount.toDouble() > 0
        val isTermValid = term.toIntOrNull() != null && term.toInt() > 0
        val termInt = term.toIntOrNull() ?: 0

        val ratesWithAvailability = RateRules.getAllRatesWithAvailability(termInt)
        val selectedRate = if (isTermValid) {
            ratesWithAvailability.firstOrNull { it.isAvailable }?.rule
        } else null

        Log.d("AdditionalVM", "Amount from navigation: '$amount'")
        Log.d("AdditionalVM", "Term from navigation: '$term'")
        Log.d("AdditionalVM", "All keys in savedStateHandle: ${savedStateHandle.keys()}")
        _uiState.value = AdditionalUiState(
            depositAmount = amount,
            depositTerm = term,
            isAmountValid = isAmountValid,
            isTermValid = isTermValid,
            ratesWithAvailability = ratesWithAvailability,
            selectedRate = selectedRate,
            canProceed = isAmountValid && isTermValid && selectedRate != null,
            errorMessage = if (!isTermValid && term.isNotBlank())
                "Пожалуйста, укажите корректный срок вклада" else null
        )
    }

    fun updateDepositTerm(term: String) {
        _uiState.update { currentState ->
            val termInt = term.toIntOrNull() ?: 0
            val isValid = term.isEmpty() || termInt > 0

            // Пересчитываем доступные ставки на основе нового срока
            val ratesWithAvailability = RateRules.getAllRatesWithAvailability(termInt)

            // Проверяем, доступна ли текущая выбранная ставка
            val currentSelectedRate = currentState.selectedRate
            val isSelectedRateStillAvailable = currentSelectedRate?.let { rate ->
                ratesWithAvailability.any { it.rule == rate && it.isAvailable }
            } ?: false

            // Выбираем первую доступную ставку, если текущая недоступна
            val selectedRate = when {
                isSelectedRateStillAvailable -> currentSelectedRate
                currentState.isTermValid && termInt > 0 ->
                    ratesWithAvailability.firstOrNull { it.isAvailable }?.rule
                else -> null
            }

            val errorMessage = when {
                term.isNotBlank() && !isValid -> "Введите корректное количество месяцев"
                term.isNotBlank() && isValid && ratesWithAvailability.none { it.isAvailable } ->
                    "Для срока ${term} месяцев нет доступных ставок"
                else -> null
            }

            currentState.copy(
                depositTerm = term,
                isTermValid = isValid,
                ratesWithAvailability = ratesWithAvailability,
                selectedRate = selectedRate,
                canProceed = currentState.isAmountValid && isValid && selectedRate != null,
                errorMessage = errorMessage
            )
        }
    }

    fun selectRate(rate: RateRule) {
        _uiState.update { currentState ->
            val isAvailable = currentState.ratesWithAvailability
                .firstOrNull { it.rule == rate }?.isAvailable ?: false
            if (isAvailable) {
                currentState.copy(
                    selectedRate = rate,
                    canProceed = currentState.isAmountValid && currentState.isTermValid
                )
            } else {
                currentState.copy(
                    errorMessage = "Эта ставка недоступна для срока ${currentState.depositTerm} месяцев"
                )
            }
        }
    }

    fun updateMonthlyAddition(addition: String) {
        _uiState.update { currentState ->
            val isValid = addition.isEmpty() || (addition.toDoubleOrNull() != null && addition.toDouble() >= 0)

            currentState.copy(
                monthlyAddition = addition,
                isAdditionValid = isValid,
                withMonthlyAddition = addition.isNotBlank() &&
                        (addition.toDoubleOrNull() != null && addition.toDouble() > 0)
            )
        }
    }

    fun calculate() {
        val currentState = _uiState.value

        // если поля неправильные, не считать
        when {
            !currentState.isAmountValid -> {
                _uiState.update { it.copy(errorMessage = "Укажите корректную сумму вклада") }
                return
            }
            !currentState.isTermValid -> {
                _uiState.update { it.copy(errorMessage = "Укажите корректный срок вклада") }
                return
            }
            currentState.selectedRate == null -> {
                _uiState.update { it.copy(errorMessage = "Выберите доступную процентную ставку") }
                return
            }
            !currentState.isAdditionValid && currentState.monthlyAddition.isNotBlank() -> {
                _uiState.update { it.copy(errorMessage = "Укажите корректную сумму пополнения") }
                return
            }
        }
    }
}