package ci.nsu.mobile.main.calculations.ui.history

import ci.nsu.mobile.main.calculations.data.database.DepositCalculationEntity

data class HistoryUiState(
    val isLoading: Boolean = false,
    val calculations: List<DepositCalculationEntity> = emptyList(),
    val errorMessage: String? = null
)
