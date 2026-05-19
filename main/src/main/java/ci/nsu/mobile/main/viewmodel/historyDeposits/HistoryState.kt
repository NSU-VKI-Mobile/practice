package ci.nsu.mobile.main.viewmodel.historyDeposits

import ci.nsu.mobile.main.data.room.DepositCalculationEntity

data class HistoryState (
    val deposits: List<DepositCalculationEntity> = emptyList(),
    val selectedDeposit: DepositCalculationEntity? = null
)