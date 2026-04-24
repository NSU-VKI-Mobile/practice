package ci.nsu.moble.main.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.app.DepositApp
import ci.nsu.moble.main.data.database.DepositCalculationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ResultState(
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,
    val saveSuccess: Boolean? = null
)

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as DepositApp).repository
    private val _state = MutableStateFlow(ResultState())
    val state: StateFlow<ResultState> = _state.asStateFlow()

    fun calculateAndShow(amount: Double, months: Int, rate: Double, topUp: Double?) {
        var total = amount
        val monthlyRate = rate / 100 / 12
        repeat(months) {
            total += total * monthlyRate
            topUp?.let { total += it }
        }
        val interest = total - amount - (topUp ?: 0.0) * months
        _state.update { it.copy(finalAmount = total, interestEarned = interest) }
    }

    fun saveCalculation(calculation: DepositCalculationEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.saveCalculation(calculation)
                _state.update { it.copy(saveSuccess = true) }
                onSuccess()
            } catch (e: Exception) {
                _state.update { it.copy(saveSuccess = false) }
            }
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return ResultViewModel(application) as T
            }
        }
    }
}