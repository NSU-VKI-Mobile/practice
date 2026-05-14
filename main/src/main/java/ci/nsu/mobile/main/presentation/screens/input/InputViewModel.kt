package ci.nsu.mobile.main.presentation.screens.input

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.domain.usecase.CalculateDepositUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InputViewModel(
    private val calculateDepositUseCase: CalculateDepositUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InputUiState())
    val uiState: StateFlow<InputUiState> = _uiState.asStateFlow()

    fun updateInitialAmount(amount: String) {
        _uiState.update {
            it.copy(
                initialAmount = amount,
                initialAmountError = when {
                    amount.isBlank() -> "Обязательное поле"
                    amount.toDoubleOrNull() == null -> "Введите корректное число"
                    amount.toDouble() <= 0 -> "Сумма должна быть больше 0"
                    else -> null
                }
            )
        }
    }

    fun updatePeriodMonths(months: String) {
        val periodInt = months.toIntOrNull()
        val availableRates = calculateDepositUseCase.getAvailableInterestRates(periodInt)

        _uiState.update {
            it.copy(
                periodMonths = months,
                periodMonthsError = when {
                    months.isBlank() -> "Обязательное поле"
                    periodInt == null -> "Введите целое число"
                    periodInt <= 0 -> "Срок должен быть больше 0"
                    else -> null
                },
                availableInterestRates = availableRates,
                selectedInterestRate = if (availableRates.size == 1) availableRates.first() else it.selectedInterestRate
            )
        }
    }

    fun updateInterestRate(rate: Double) {
        _uiState.update { it.copy(selectedInterestRate = rate) }
    }

    fun updateMonthlyTopUp(topUp: String) {
        _uiState.update {
            it.copy(
                monthlyTopUp = topUp,
                monthlyTopUpError = if (topUp.isNotBlank() && (topUp.toDoubleOrNull() == null || topUp.toDouble() < 0)) {
                    "Введите положительное число"
                } else null
            )
        }
    }

    fun isValidStep1(): Boolean = _uiState.value.initialAmountError == null && _uiState.value.periodMonthsError == null
    fun isValidStep2(): Boolean = _uiState.value.selectedInterestRate != null && _uiState.value.monthlyTopUpError == null
}

data class InputUiState(
    val initialAmount: String = "",
    val initialAmountError: String? = null,
    val periodMonths: String = "",
    val periodMonthsError: String? = null,
    val availableInterestRates: List<Double> = emptyList(),
    val selectedInterestRate: Double? = null,
    val monthlyTopUp: String = "",
    val monthlyTopUpError: String? = null
)

