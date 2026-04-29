package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repositories.DepositRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    // Список расчётов
    private val _calculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val calculations: StateFlow<List<DepositCalculation>> = _calculations.asStateFlow()

    // Выбранный расчёт для деталей
    private val _selectedCalculation = MutableStateFlow<DepositCalculation?>(null)
    val selectedCalculation: StateFlow<DepositCalculation?> = _selectedCalculation.asStateFlow()

    // Состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Ошибка
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                withContext(Dispatchers.IO) {
                    _calculations.value = repository.getAllCalculations()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCalculation(id: Long) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    _selectedCalculation.value = repository.getCalculationById(id)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun refresh() {
        loadCalculations()
    }
}