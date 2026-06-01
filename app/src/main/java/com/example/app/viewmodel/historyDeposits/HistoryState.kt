package com.example.app.viewmodel.historyDeposits

import com.example.domain.models.DepositCalculation
import com.example.domain.models.DepositFilter


data class HistoryState (
    val deposits: List<DepositCalculation> = emptyList(),
    val selectedDeposit: DepositCalculation? = null,
    val filter: DepositFilter = DepositFilter(),
    val startAmount: String = "",
    val endAmount: String = "",
    val date: String = "",
    val dateMillis: Long? = null,
    val errorFieldsAmount: Set<String> = emptySet(),
    val isFilterOpen: Boolean = false,
    val showDatePicker: Boolean = false,
    val showDDMenu: Boolean = false,
    val rates: List<Int> = listOf(15, 10, 5),
    val selectedRate: Int? = null
)

sealed class HistoryEvents {
    data class SelectedDepositUpdate(val newDeposit: DepositCalculation) : HistoryEvents()
    data class DeleteDeposit(val depositId: Long) : HistoryEvents()
    data class IsFilterOpenUpdate(val newValue: Boolean) : HistoryEvents()
    data class SelectedRateChanged(val newRate: Int?) : HistoryEvents()
    object FilterUp : HistoryEvents()
    data class StartAmountChanged(val newAmount: String) : HistoryEvents()
    data class EndAmountChanged(val newAmount: String) : HistoryEvents()
    data class DateChanged(val newDate: String) : HistoryEvents()
    data class DatePickerVisibilityChanged(val newState: Boolean) : HistoryEvents()
    data class DateMillisChanged(val newDateMillis: Long?) : HistoryEvents()
    data class MenuStateChanged(val newState: Boolean) : HistoryEvents()
    object LoadHistory : HistoryEvents()
    object ValidationFilter : HistoryEvents()
    object ResetFilter : HistoryEvents()
}