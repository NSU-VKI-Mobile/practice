package ci.nsu.mobile.calculations.ui.history

import ci.nsu.mobile.calculations.data.database.DepositCalculationEntity

data class HistoryUiState(
    val isLoading: Boolean = false,
    val calculations: List<DepositCalculationEntity> = emptyList(),
    val errorMessage: String? = null
)
