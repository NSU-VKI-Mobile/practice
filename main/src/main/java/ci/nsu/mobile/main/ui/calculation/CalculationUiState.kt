package ci.nsu.mobile.main.ui.calculation

data class CalculationUiState(
    val depositAmount: String = "",
    val depositTerm: String = "",
    val isAmountValid: Boolean = true,
    val isTermValid: Boolean = true,
    val canProceed: Boolean = false,
    val errorMessage: String? = null
)
