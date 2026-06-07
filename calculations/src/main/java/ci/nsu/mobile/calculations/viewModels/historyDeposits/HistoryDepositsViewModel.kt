package ci.nsu.mobile.calculations.viewModels.historyDeposits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.calculations.data.repository.DepositFilter
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.domain.models.DepositCalculation
import ci.nsu.mobile.domain.token.ITokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDepositsViewModel @Inject constructor(
    val repository: DepositRepository,
    val tokenManager: ITokenManager
): ViewModel()
{
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    fun historyEvent(event: HistoryEvents) {
        when(event) {
            is HistoryEvents.DeleteDeposit -> deleteDeposit(event.deposit)
            is HistoryEvents.LoadHistory -> loadHistory()
            is HistoryEvents.SelectedDepositUpdate -> {
                _state.update { it.copy(selectedDeposit = event.newDeposit) }
            }
            is HistoryEvents.StartAmountChanged -> {
                _state.update { it.copy(startAmount = event.newAmount,
                    errorFieldsAmount = it.errorFieldsAmount - "start") }
            }
            is HistoryEvents.EndAmountChanged -> {
                _state.update { it.copy(endAmount = event.newAmount,
                    errorFieldsAmount = it.errorFieldsAmount - "end") }
            }
            is HistoryEvents.IsFilterOpenUpdate -> {
                _state.update { it.copy(isFilterOpen = event.newValue) }
            }
            is HistoryEvents.DatePickerVisibilityChanged -> {
                _state.update { it.copy(showDatePicker = event.newState) }
            }
            is HistoryEvents.FilterUp -> loadHistory()
            is HistoryEvents.ValidationFilter -> if (validationFilter()) updateFilter()
            is HistoryEvents.ResetFilter -> resetFilter()
            is HistoryEvents.DateChanged -> {
                _state.update { it.copy(date = event.newDate) }
            }
            is HistoryEvents.DateMillisChanged -> {
                _state.update { it.copy(dateMillis = event.newDateMillis) }
            }
            is HistoryEvents.MenuStateChanged -> {
                _state.update { it.copy(showDDMenu = event.newState) }
            }
            is HistoryEvents.SelectedRateChanged -> {
                _state.update { it.copy(selectedRate = event.newRate) }
            }
        }
    }

    private fun resetFilter() {
        _state.update {
            it.copy(
                filter = DepositFilter(),
                startAmount = "",
                endAmount = "",
                date = "",
                dateMillis = null,
                errorFieldsAmount = emptySet(),
                isFilterOpen = false,
                selectedRate = null
            )
        }
        loadHistory()
    }
    private fun validationFilter(): Boolean {
        val stateValue = _state.value
        val errorFields = mutableSetOf<String>()
        stateValue.startAmount.toDoubleOrNull()?.let { value ->
            if (value < 0.0) errorFields.add("start")
        } ?: run {
        }

        stateValue.endAmount.toDoubleOrNull()?.let { value ->
            if (value < 0.0) errorFields.add("end")
        } ?: run {
        }

        _state.update { it.copy(errorFieldsAmount = errorFields) }
        return errorFields.isEmpty()
    }
    private fun updateFilter() {
        val values = _state.value
        val filter = DepositFilter(
            values.startAmount.toDoubleOrNull(),
            values.endAmount.toDoubleOrNull(),
            date = values.dateMillis,
            rate = values.selectedRate
        )
        _state.update { it.copy(filter = filter) }
    }
    private fun deleteDeposit(deposit: DepositCalculation) {
        viewModelScope.launch {
            repository.deleteDeposit(deposit)
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            val userId = tokenManager.userId?.toLong() ?: return@launch  // Исправлено
            repository.getFiltered(userId, _state.value.filter).collect { deposits ->
                _state.update { it.copy(deposits = deposits) }
            }
        }
    }
}