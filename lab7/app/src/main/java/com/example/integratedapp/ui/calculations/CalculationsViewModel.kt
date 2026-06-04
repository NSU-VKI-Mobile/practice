package com.example.integratedapp.ui.calculations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integratedapp.data.SessionManager
import com.example.integratedapp.data.db.DepositCalculation
import com.example.integratedapp.data.repository.DepositRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Состояние списка расчётов
data class CalculationsUiState(
    val calculations: List<DepositCalculation> = emptyList(),
    val selectedCalculation: DepositCalculation? = null,
    // Фильтры
    val minAmount: String = "",
    val maxAmount: String = ""
)

class CalculationsViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    // Получаем расчёты текущего пользователя из базы
    // Flow автоматически обновляется при добавлении/удалении записей
    private val allCalculations: Flow<List<DepositCalculation>> =
        repository.getByUser(SessionManager.userId)

    private val _filterState = MutableStateFlow(CalculationsUiState())

    // Объединяем поток данных из базы и состояние фильтров
    val uiState: StateFlow<CalculationsUiState> = combine(
        allCalculations,
        _filterState
    ) { calcs, filter ->
        // Применяем фильтры
        val filtered = calcs.filter { calc ->
            val min = filter.minAmount.toDoubleOrNull() ?: 0.0
            val max = filter.maxAmount.toDoubleOrNull() ?: Double.MAX_VALUE
            calc.initialAmount in min..max
        }
        filter.copy(calculations = filtered)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalculationsUiState()
    )

    fun onMinAmountChanged(value: String) {
        _filterState.update { it.copy(minAmount = value) }
    }

    fun onMaxAmountChanged(value: String) {
        _filterState.update { it.copy(maxAmount = value) }
    }

    fun selectCalculation(calc: DepositCalculation?) {
        _filterState.update { it.copy(selectedCalculation = calc) }
    }

    fun delete(calc: DepositCalculation) {
        viewModelScope.launch {
            repository.delete(calc)
            _filterState.update { it.copy(selectedCalculation = null) }
        }
    }
}
