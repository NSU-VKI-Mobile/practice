package ci.nsu.mobile.main.ui.additional

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.domain.model.RateRules
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AdditionalViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdditionalUiState())
    val uiState: StateFlow<AdditionalUiState> = _uiState.asStateFlow()

    companion object {
        private const val KEY_AMOUNT = "deposit_amount"
        private const val KEY_PERIOD = "deposit_period"
        fun createArgs(amount: String, period: String): Map<String, String> {
            return mapOf(
                KEY_AMOUNT to amount,
                KEY_PERIOD to period
            )
        }
    }

    init {
        // Получаем данные из SavedStateHandle
        val amount = savedStateHandle.get<String>(KEY_AMOUNT) ?: ""
        val period = savedStateHandle.get<String>(KEY_PERIOD) ?: ""

        initialize(amount, period)
    }

    private fun initialize(amount: String, period: String) {

        /*val ratesWithAvailability = if (period > 0) {
            RateRules.getAllRatesWithAvailability(periodInt)
        } else {
            emptyList()
        }

        val defaultRate = ratesWithAvailability
            .firstOrNull { it.isAvailable }
            ?.rule

        _uiState.update {
            it.copy(
                depositAmount = amount,
                depositTerm = period,
                ratesWithAvailability = ratesWithAvailability,
                selectedRate = defaultRate,
            )
        } */
    }

}