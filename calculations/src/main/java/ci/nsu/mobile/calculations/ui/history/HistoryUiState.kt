package ci.nsu.mobile.calculations.ui.history

import ci.nsu.mobile.domain.calculations.DepositCalculation

data class HistoryUiState(
    val isLoading: Boolean = false,
    val calculations: List<DepositCalculation> = emptyList(),
    val errorMessage: String? = null
)
