package ci.nsu.mobile.main.calculations.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.calculations.data.repository.DepositRepository
import ci.nsu.mobile.main.calculations.data.database.DepositCalculationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// Группы фильтров
data class AmountRange(val min: Double?, val max: Double?)
data class TermRange(val min: Int?, val max: Int?)
data class DateRange(val from: Long?, val to: Long?)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val depositRepository: DepositRepository
) : ViewModel() {

    // Отдельные потоки для групп фильтров
    private val _amountRange = MutableStateFlow(AmountRange(null, null))
    private val _termRange = MutableStateFlow(TermRange(null, null))
    private val _dateRange = MutableStateFlow(DateRange(null, null))

    // Все расчеты пользователя
    private val allCalculations = depositRepository.getDepositsForCurrentUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Объединяем все потоки (всего 4 параметра)
    val uiState: StateFlow<HistoryUiState> = combine(
        allCalculations,
        _amountRange,
        _termRange,
        _dateRange
    ) { calculations, amountRange, termRange, dateRange ->
        val filtered = calculations.filter { calc ->
            var matches = true

            // Фильтр по сумме
            amountRange.min?.let { if (calc.initialAmount < it) matches = false }
            amountRange.max?.let { if (calc.initialAmount > it) matches = false }

            // Фильтр по сроку
            termRange.min?.let { if (calc.periodMonths < it) matches = false }
            termRange.max?.let { if (calc.periodMonths > it) matches = false }

            // Фильтр по дате
            dateRange.from?.let { if (calc.calculationDate < it) matches = false }
            dateRange.to?.let { if (calc.calculationDate > it) matches = false }

            matches
        }

        HistoryUiState(
            isLoading = false,
            calculations = filtered,
            errorMessage = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState(isLoading = true)
    )

    // Выбранный расчет
    private val _selectedCalculation = MutableStateFlow<DepositCalculationEntity?>(null)
    val selectedCalculation: StateFlow<DepositCalculationEntity?> = _selectedCalculation.asStateFlow()

    fun selectCalculation(calculation: DepositCalculationEntity) {
        _selectedCalculation.value = calculation
    }

    fun clearSelectedCalculation() {
        _selectedCalculation.value = null
    }

    fun deleteCalculation(calculation: DepositCalculationEntity) {
        viewModelScope.launch {
            depositRepository.deleteDeposit(calculation)
            if (_selectedCalculation.value == calculation) {
                _selectedCalculation.value = null
            }
        }
    }

    // Обновление фильтров
    fun setAmountRange(min: String?, max: String?) {
        _amountRange.value = AmountRange(
            min = min?.toDoubleOrNull(),
            max = max?.toDoubleOrNull()
        )
    }

    fun setTermRange(min: String?, max: String?) {
        _termRange.value = TermRange(
            min = min?.toIntOrNull(),
            max = max?.toIntOrNull()
        )
    }

    fun setDateRange(from: Long?, to: Long?) {
        _dateRange.value = DateRange(from, to)
    }

    fun clearFilters() {
        _amountRange.value = AmountRange(null, null)
        _termRange.value = TermRange(null, null)
        _dateRange.value = DateRange(null, null)
    }
}
