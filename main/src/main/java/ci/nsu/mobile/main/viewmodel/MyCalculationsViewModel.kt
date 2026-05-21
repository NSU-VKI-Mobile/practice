package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repositories.DepositRepository
import ci.nsu.mobile.main.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyCalculationsViewModel(
    private val repository: DepositRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _calculations = MutableStateFlow<List<DepositCalculation>>(emptyList())
    val calculations: StateFlow<List<DepositCalculation>> = _calculations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _deletingId = MutableStateFlow<Long?>(null)
    val deletingId: StateFlow<Long?> = _deletingId.asStateFlow()

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val userId = userPreferences.getUserId() ?: 0L
                _calculations.value = repository.getCalculationsByUserId(userId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            _deletingId.value = id
            try {
                val userId = userPreferences.getUserId() ?: 0L
                val success = repository.deleteCalculationById(id, userId)
                if (success) {
                    _calculations.value = _calculations.value.filter { it.id != id }
                } else {
                    _error.value = "Не удалось удалить расчёт"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _deletingId.value = null
            }
        }
    }

    fun refresh() = loadCalculations()
}