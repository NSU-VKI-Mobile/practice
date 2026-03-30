package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ci.nsu.mobile.main.domain.calculateInterestRateForPeriod
import ci.nsu.mobile.main.domain.computeDeposit
import ci.nsu.mobile.main.util.Event

class DepositStage2ViewModel(application: Application) : AndroidViewModel(application) {

    data class Stage2Inputs(
        val initialAmount: Double,
        val periodMonths: Int,
        val interestRate: Double,
        val monthlyTopUp: Double?,
        val finalAmount: Double,
        val interestEarned: Double,
    )

    data class UiState(
        val initialAmount: Double? = null,
        val periodMonthsText: String = "",
        val monthlyTopUpText: String = "",
        val periodMonthsError: String? = null,
        val monthlyTopUpError: String? = null,
        val interestRateOptions: List<Double> = emptyList(),
        val selectedInterestRate: Double? = null,
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _navigationToResult = MutableLiveData<Event<Stage2Inputs>>()
    val navigationToResult: LiveData<Event<Stage2Inputs>> = _navigationToResult

    private var initialized = false

    fun setInitialAmountAndPeriodOnce(initialAmount: Double, periodMonths: Int) {
        if (initialized) return
        initialized = true
        _uiState.value = _uiState.value?.copy(
            initialAmount = initialAmount,
            periodMonthsText = periodMonths.toString()
        )
    }

    fun onPeriodMonthsChanged(text: String) {
        val prev = _uiState.value ?: UiState()
        val trimmed = text.trim()

        val parsedPeriod = trimmed.toIntOrNull()
        val (periodError, options, selected) = when {
            trimmed.isEmpty() -> Triple("Срок не указан", emptyList<Double>(), null)
            parsedPeriod == null || parsedPeriod <= 0 -> Triple("Введите корректный срок (месяцы, > 0)", emptyList(), null)
            else -> {
                val rate = calculateInterestRateForPeriod(parsedPeriod)
                Triple(null, listOf(rate), rate)
            }
        }

        _uiState.value = prev.copy(
            periodMonthsText = text,
            periodMonthsError = periodError,
            interestRateOptions = options,
            selectedInterestRate = selected
        )
    }

    fun onMonthlyTopUpChanged(text: String) {
        val prev = _uiState.value ?: UiState()
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            _uiState.value = prev.copy(monthlyTopUpText = text, monthlyTopUpError = null)
            return
        }

        val parsed = trimmed.toDoubleOrNull()
        if (parsed == null || parsed < 0.0) {
            _uiState.value = prev.copy(monthlyTopUpText = text, monthlyTopUpError = "Введите корректное пополнение (>= 0)")
        } else {
            _uiState.value = prev.copy(monthlyTopUpText = text, monthlyTopUpError = null)
        }
    }

    fun onBackToStage1() {
        // Navigation handled by Activity (no one-time event needed).
    }

    fun onCalculateClicked() {
        val current = _uiState.value ?: UiState()
        val initialAmount = current.initialAmount
        if (initialAmount == null) return

        val periodMonthsValue = current.periodMonthsText.trim().toIntOrNull()?.takeIf { it > 0 }
        if (periodMonthsValue == null) {
            _uiState.value = current.copy(periodMonthsError = "Срок не указан")
            return
        }

        val monthlyTopUp: Double? = when (current.monthlyTopUpText.trim()) {
            "" -> null
            else -> {
                val parsed = current.monthlyTopUpText.trim().toDoubleOrNull()
                if (parsed == null || parsed < 0.0) {
                    // Reuse error field but keep UX simple
                    _uiState.value = current.copy(monthlyTopUpError = "Введите корректное пополнение (>= 0)")
                    return
                }
                parsed
            }
        }

        val interestRate = calculateInterestRateForPeriod(periodMonthsValue)
        val computed = computeDeposit(
            initialAmount = initialAmount,
            periodMonths = periodMonthsValue,
            monthlyTopUp = monthlyTopUp,
            interestRate = interestRate
        )

        _navigationToResult.value = Event(
            Stage2Inputs(
                initialAmount = initialAmount,
                periodMonths = periodMonthsValue,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = computed.finalAmount,
                interestEarned = computed.interestEarned,
            )
        )
    }
}

