package ci.nsu.mobile.calculations.ui.additional

import ci.nsu.mobile.calculations.domain.model.RateRule
import ci.nsu.mobile.calculations.domain.model.RateWithAvailability

data class AdditionalUiState(
    val depositAmount: String = "",
    val depositTerm: String = "",

    val isAmountValid: Boolean = false,
    val isTermValid: Boolean = false,

    val ratesWithAvailability: List<RateWithAvailability> = emptyList(),
    val selectedRate: RateRule? = null,

    val withMonthlyAddition: Boolean = false,
    val monthlyAddition: String = "",
    val isAdditionValid: Boolean = false,

    val canProceed: Boolean = false,
    val errorMessage: String? = null,
)