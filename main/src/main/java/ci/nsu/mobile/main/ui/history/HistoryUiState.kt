package ci.nsu.mobile.main.ui.history

import ci.nsu.mobile.main.data.database.DepositCalculationEntity

data class HistoryUiState(
    val isLoading: Boolean = false,
    val calculations: List<DepositCalculationEntity> = emptyList(),
    val selectedCalculation: DepositCalculationEntity? = null,
    val errorMessage: String? = null
)