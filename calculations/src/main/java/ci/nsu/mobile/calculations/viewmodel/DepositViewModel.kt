package ci.nsu.mobile.calculations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.calculations.data.local.entities.DepositCalculation
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)

    val myCalculations: Flow<List<DepositCalculation>> = _userId
        .flatMapLatest { userId ->
            if (userId != null) repository.getCalculationsForUser(userId)
            else flowOf(emptyList())
        }
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess

    fun setUserId(userId: Long) {
        _userId.value = userId
    }

    fun saveCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.saveCalculation(calculation)
                _saveSuccess.emit(Unit)
            } catch (e: Exception) {
                _error.emit("Failed to save: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getCalculationById(id: Long): DepositCalculation? {
        val userId = _userId.value ?: return null
        return try {
            repository.getCalculationById(id, userId)
        } catch (e: Exception) {
            _error.emit("Failed to load calculation: ${e.message}")
            null
        }
    }

    fun deleteCalculation(calculation: DepositCalculation) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteCalculation(calculation)
            } catch (e: Exception) {
                _error.emit("Failed to delete: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun calculateDeposit(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double? = null
    ): DepositResult {
        val monthlyRate = interestRate / 100 / 12
        var currentAmount = initialAmount
        var totalInterest = 0.0

        for (month in 1..periodMonths) {
            val monthlyInterest = currentAmount * monthlyRate
            totalInterest += monthlyInterest
            currentAmount += monthlyInterest
            monthlyTopUp?.let { currentAmount += it }
        }

        return DepositResult(
            finalAmount = currentAmount,
            interestEarned = totalInterest
        )
    }
}

data class DepositResult(
    val finalAmount: Double,
    val interestEarned: Double
)