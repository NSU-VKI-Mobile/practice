package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.dao.DepositDao
import ci.nsu.moble.main.data.entities.DepositCalculationEntity
import ci.nsu.moble.main.viewmodel.states.DepositUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CalcScreensViewModel(
    private val dao: DepositDao,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState = _uiState.asStateFlow()

    fun updateAmount(newAmount: String) { _uiState.update { it.copy(amount = newAmount) } }
    fun updateDuration(newDuration: String) { _uiState.update { it.copy(duration = newDuration) } }
    fun updateMonthlyAdd(newMonthlyAdd: String) { _uiState.update { it.copy(monthlyAdd = newMonthlyAdd) } }
    fun clearFields() { _uiState.value = DepositUiState() }

    fun getRate(): Double {
        val state = _uiState.value
        val m = state.duration.toIntOrNull() ?: return 0.0
        return when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }
    }

    fun calculate(): DepositCalculationEntity {
        val state = _uiState.value
        val p = state.amount.toDoubleOrNull() ?: 0.0
        val n = state.duration.toIntOrNull() ?: 0
        val m = state.monthlyAdd.toDoubleOrNull() ?: 0.0

        var total = p
        repeat(n) { total = (total + m) * (1 + (getRate() / 100 / 12)) }

        val currentUserId = tokenManager.currentUser.value?.userId
            ?: throw IllegalStateException("No UserId found")

        return DepositCalculationEntity(
            userId = currentUserId,
            initialAmount = p,
            periodMonths = n,
            interestRate = getRate(),
            monthlyTopUp = m,
            finalAmount = total,
            interestEarned = total - p - (m * n),
            calculationDate = System.currentTimeMillis()
        )
    }

    // Метод сохранения оставляем, так как кнопка "Сохранить результат" находится на экране ResultScreen
    fun save(record: DepositCalculationEntity) = viewModelScope.launch {
        dao.insert(record)
    }
}