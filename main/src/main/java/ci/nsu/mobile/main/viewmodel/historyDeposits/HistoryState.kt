package ci.nsu.mobile.main.viewmodel.historyDeposits

import ci.nsu.mobile.main.data.room.DepositCalculationEntity

data class HistoryState (
    val deposits: List<DepositCalculationEntity> = emptyList(),
    val selectedDeposit: DepositCalculationEntity? = null
)

sealed class HistoryEvents {
    data class SelectedDepositUpdate(val newDeposit: DepositCalculationEntity): HistoryEvents()
    data class DeleteDeposit(val deposit: DepositCalculationEntity): HistoryEvents()
    object LoadHistory: HistoryEvents()
}