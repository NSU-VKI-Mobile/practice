package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculationViewModel : ViewModel() {

    private val _calculations = MutableStateFlow<List<Calculation>>(emptyList())
    val calculations: StateFlow<List<Calculation>> = _calculations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val database = AppDatabase.getDatabase(ApplicationClass.getAppContext())
                val calculationsList = database.calculationDao().getAllCalculationsList()
                _calculations.value = calculationsList
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveCalculation(calculation: Calculation) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val database = AppDatabase.getDatabase(ApplicationClass.getAppContext())
                database.calculationDao().insertCalculation(calculation)
                _saveSuccess.value = true
                loadCalculations()
                kotlinx.coroutines.delay(2000)
                _saveSuccess.value = false
            } catch (e: Exception) {
                _error.value = "Ошибка сохранения: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCalculation(calculation: Calculation) {
        viewModelScope.launch {
            try {
                val database = AppDatabase.getDatabase(ApplicationClass.getAppContext())
                database.calculationDao().deleteCalculation(calculation)
                loadCalculations()
            } catch (e: Exception) {
                _error.value = "Ошибка удаления: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    fun calculateResult(startAmount: Double, termMonths: Int, interestRate: Double, monthlyDeposit: Double): Pair<Double, Double> {
        val monthlyRate = interestRate / 100 / 12
        var totalAmount = startAmount
        for (i in 1..termMonths) {
            totalAmount += totalAmount * monthlyRate
            totalAmount += monthlyDeposit
        }
        val totalProfit = totalAmount - startAmount - (monthlyDeposit * termMonths)
        return Pair(totalAmount, totalProfit)
    }

    fun getAvailableRates(termMonths: Int): List<InterestRate> {
        return listOf(
            InterestRate(12.0, "12% (для вкладов до 6 месяцев)", termMonths < 6 && termMonths > 0),
            InterestRate(10.0, "10% (для вкладов от 6 до 12 месяцев)", termMonths in 6..11),
            InterestRate(5.0, "5% (для вкладов от 12 месяцев)", termMonths >= 12)
        )
    }

    fun clearError() {
        _error.value = null
    }
}

data class InterestRate(
    val rate: Double,
    val description: String,
    val isAvailable: Boolean
)