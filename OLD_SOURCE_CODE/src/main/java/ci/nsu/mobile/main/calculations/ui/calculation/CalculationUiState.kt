package ci.nsu.mobile.main.calculations.ui.calculation

data class CalculationUiState(
    val depositAmount: String = "",
    val depositTerm: String = "",
    val isAmountValid: Boolean = false,
    val isTermValid: Boolean = false,
    val canProceed: Boolean = false,
    val errorMessage: String? = null
)