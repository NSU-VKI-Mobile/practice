package ci.nsu.mobile.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepCalcs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultViewModel(
    private val database: AppDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ResultUiState>(ResultUiState.Idle)
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    fun calculateAndSave(amount: Double, months: Int, rate: Double, topUp: Double) {
        viewModelScope.launch {
            _uiState.value = ResultUiState.Loading

            try {
                val monthlyRate = rate / 100 / 12
                val totalAmount = calculateDeposit(amount, months, monthlyRate, topUp)
                val totalInterest = totalAmount - amount - (topUp * months)

                val calculation = DepCalcs(
                    initialAmount = amount,
                    interestRate = rate,
                    periodMonths = months,
                    monthlyTopUp = topUp,
                    finalAmount = totalAmount,
                    interestEarned = totalInterest,
                    calculationDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
                )

                database.depositDao().insert(calculation)

                _uiState.value = ResultUiState.Success(calculation)
            } catch (e: Exception) {
                _uiState.value = ResultUiState.Error(e.message ?: "Ошибка расчёта")
            }
        }
    }

    private fun calculateDeposit(
        initialAmount: Double,
        months: Int,
        monthlyRate: Double,
        monthlyTopUp: Double
    ): Double {
        var total = initialAmount
        for (i in 1..months) {
            total += total * monthlyRate
            total += monthlyTopUp
        }
        return total
    }
}