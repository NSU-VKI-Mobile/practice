package ci.nsu.mobile.main.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.model.DepositCalculation
import ci.nsu.mobile.main.data.remote.UserManager
import ci.nsu.mobile.main.data.remote.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val depositRepository: DepositRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _step1Data = MutableStateFlow<Pair<Double, Int>?>(null)
    val step1Data = _step1Data.asStateFlow()

    private val _currentCalculation = MutableStateFlow<DepositCalculation?>(null)
    val currentCalculation = _currentCalculation.asStateFlow()

    init {
        loadUsers()
        loadCalculations()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingUsers = true)
            when (val result = authRepository.getUsers()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingUsers = false,
                        users = result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingUsers = false,
                        error = result.exception.message ?: "Ошибка загрузки"
                    )
                }
            }
        }
    }

    // Загрузка расчётов текущего пользователя
    private fun loadCalculations() {
        viewModelScope.launch {
            val userId = UserManager.userId
            if (userId != -1L) {
                depositRepository.getCalculationsByUser(userId).collect { calculations ->
                    _uiState.value = _uiState.value.copy(calculations = calculations)
                }
            }
        }
    }
    fun setStep1Data(initialAmount: Double, periodMonths: Int) {
        _step1Data.value = Pair(initialAmount, periodMonths)
    }

    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        rate: Double,
        monthlyTopUp: Double?
    ) {
        var total = initialAmount
        for (i in 1..periodMonths) {
            total += total * rate / 100 / 12
            if (monthlyTopUp != null) total += monthlyTopUp
        }
        val interestEarned = total - initialAmount -
                (monthlyTopUp ?: 0.0) * periodMonths

        _currentCalculation.value = DepositCalculation(
            userId = UserManager.userId ,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = rate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = total,
            interestEarned = interestEarned
        )
    }

    fun saveCurrentCalculation(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _currentCalculation.value?.let {
                depositRepository.insert(it)
                onSuccess()
            }
        }
    }

    fun deleteCalculation(id: Long) {
        viewModelScope.launch {
            depositRepository.deleteById(id)
        }
    }

    fun logout(onLogout: () -> Unit) {
        ci.nsu.mobile.main.data.remote.TokenManager.clearToken()
        UserManager.clearUser()
        onLogout()
    }
}

data class MainUiState(
    val isLoadingUsers: Boolean = false,
    val users: List<UserDto> = emptyList(),
    val calculations: List<DepositCalculation> = emptyList(),
    val error: String? = null
)
