package ci.nsu.mobile.main.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isUserLoggedIn by mutableStateOf(TokenManager.token != null && TokenManager.userId != -1)

    var usersList by mutableStateOf<List<UserDto>>(emptyList())
    var groupsList by mutableStateOf<List<GroupDto>>(emptyList())

    fun loadGroups() {
        viewModelScope.launch {
            repository.getGroups().onSuccess { groupsList = it }
                .onFailure { errorMessage = "Ошибка загрузки групп: ${it.message}" }
        }
    }

    fun login(login: String, pass: String) {
        if (login.isBlank() || pass.isBlank()) {
            errorMessage = "Заполните все поля"
            return
        }
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            repository.login(login, pass).onSuccess { response ->
                TokenManager.token = response.token
                repository.getUsers().onSuccess { users ->
                    val myProfile = users.find { it.login == login }
                    TokenManager.userId = myProfile?.userId ?: -1
                    isUserLoggedIn = true
                }
            }.onFailure { errorMessage = "Ошибка входа: ${it.message}" }
            isLoading = false
        }
    }

    fun register(request: RegisterRequest, onSuccess: () -> Unit) {
        isLoading = true
        viewModelScope.launch {
            repository.register(request).onSuccess { onSuccess() }
                .onFailure { errorMessage = "Ошибка регистрации: ${it.message}" }
            isLoading = false
        }
    }

    fun loadUsers() {
        isLoading = true
        viewModelScope.launch {
            repository.getUsers().onSuccess { usersList = it }
                .onFailure { errorMessage = "Не удалось загрузить пользователей" }
            isLoading = false
        }
    }

    fun logout() {
        TokenManager.clear()
        isUserLoggedIn = false
    }
}

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {
    var initialAmount by mutableStateOf("")
    var periodMonths by mutableStateOf("")
    var interestRate by mutableDoubleStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")
    var finalAmount by mutableDoubleStateOf(0.0)
    var interestEarned by mutableDoubleStateOf(0.0)

    val history: Flow<List<DepositCalculation>> = if (TokenManager.userId != -1) {
        repository.getHistory(TokenManager.userId)
    } else { emptyFlow() }

    fun determineInterestRate(): Double {
        val months = periodMonths.toIntOrNull() ?: return 0.0
        return when {
            months < 6 -> 15.0
            months in 6..11 -> 10.0
            months >= 12 -> 5.0
            else -> 0.0
        }
    }

    fun calculateResult() {
        val amount = initialAmount.toDoubleOrNull() ?: 0.0
        val months = periodMonths.toIntOrNull() ?: 0
        val rate = interestRate
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            total += topUp
            val currentMonthInterest = total * monthlyRate
            earned += currentMonthInterest
            total += currentMonthInterest
        }
        finalAmount = total
        interestEarned = earned
    }

    fun saveCalculation() {
        val calc = DepositCalculation(
            userId = TokenManager.userId,
            initialAmount = initialAmount.toDoubleOrNull() ?: 0.0,
            periodMonths = periodMonths.toIntOrNull() ?: 0,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp.toDoubleOrNull() ?: 0.0,
            finalAmount = finalAmount,
            interestEarned = interestEarned
        )
        viewModelScope.launch { repository.insert(calc) }
    }

    fun clearData() {
        initialAmount = ""
        periodMonths = ""
        interestRate = 0.0
        monthlyTopUp = ""
    }
}

class ViewModelFactory(
    private val authRepo: AuthRepository,
    private val depositRepo: DepositRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) return AuthViewModel(authRepo) as T
        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) return DepositViewModel(depositRepo) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}