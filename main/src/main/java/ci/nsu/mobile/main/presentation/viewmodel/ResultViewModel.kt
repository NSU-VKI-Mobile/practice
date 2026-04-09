package ci.nsu.mobile.main.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.DepositApp
import ci.nsu.mobile.main.data.database.DepCalcs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    // Получаем репозиторий через наш класс приложения
    private val repository = (application as DepositApp).repository

    // Состояние экрана (Idle -> Loading -> Success/Error)
    private val _uiState = MutableStateFlow<ResultUiState>(ResultUiState.Idle)
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    fun calculateAndSave(
        amount: Double,
        months: Int,
        rate: Double,
        topUp: Double
    ) {
        // Запускаем корутину в фоне (не блокирует UI)
        viewModelScope.launch {
            _uiState.value = ResultUiState.Loading
            try {
                // Формула сложных процентов с ежемесячным пополнением
                val monthlyRate = rate / 100 / 12
                var currentSum = amount

                for (i in 1..months) {
                    currentSum = currentSum * (1 + monthlyRate) + topUp
                }

                val finalAmount = currentSum
                val interestEarned = finalAmount - amount - (topUp * months)

                // Создаём объект для БД
                val calculation = DepCalcs(
                    initialAmount = amount,
                    periodMonths = months,
                    interestRate = rate,
                    monthlyTopUp = if (topUp > 0) topUp else null,
                    finalAmount = finalAmount,
                    interestEarned = interestEarned,
                    calculationDate = System.currentTimeMillis()
                )

                // Сохраняем в БД
                repository.insertCalculation(calculation)

                // Сообщаем UI, что всё готово
                _uiState.value = ResultUiState.Success(calculation)
            } catch (e: Exception) {
                _uiState.value = ResultUiState.Error(e.message ?: "Ошибка сохранения")
            }
        }
    }
}

sealed class ResultUiState {
    object Idle : ResultUiState()
    object Loading : ResultUiState()
    data class Success(val calculation: DepCalcs) : ResultUiState()
    data class Error(val message: String) : ResultUiState()
}