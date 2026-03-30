package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ci.nsu.mobile.main.util.Event

class DepositStage1ViewModel(application: Application) : AndroidViewModel(application) {

    data class Stage1Inputs(
        val initialAmount: Double,
        val periodMonths: Int,
    )

    data class UiState(
        val initialAmountText: String = "",
        val periodMonthsText: String = "",
        val initialAmountError: String? = null,
        val periodMonthsError: String? = null,
    )

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _navigationToStage2 = MutableLiveData<Event<Stage1Inputs>>()
    val navigationToStage2: LiveData<Event<Stage1Inputs>> = _navigationToStage2

    fun onInitialAmountChanged(text: String) {
        val prev = _uiState.value ?: UiState()
        _uiState.value = prev.copy(initialAmountText = text, initialAmountError = null)
    }

    fun onPeriodMonthsChanged(text: String) {
        val prev = _uiState.value ?: UiState()
        _uiState.value = prev.copy(periodMonthsText = text, periodMonthsError = null)
    }

    fun onNextClicked() {
        val current = _uiState.value ?: UiState()

        val initialAmount = current.initialAmountText.trim().toDoubleOrNull()
        val periodMonths = current.periodMonthsText.trim().toIntOrNull()

        var initialError: String? = null
        var periodError: String? = null

        if (initialAmount == null || initialAmount <= 0.0) {
            initialError = "Введите стартовый взнос (> 0)"
        }
        if (periodMonths == null || periodMonths <= 0) {
            periodError = "Введите срок вклада (месяцы, > 0)"
        }

        if (initialError != null || periodError != null) {
            _uiState.value = current.copy(
                initialAmountError = initialError,
                periodMonthsError = periodError
            )
            return
        }

        _navigationToStage2.value = Event(Stage1Inputs(initialAmount = initialAmount!!, periodMonths = periodMonths!!))
    }
}

