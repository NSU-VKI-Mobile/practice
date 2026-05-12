package ci.nsu.mobile.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.dbo.AppDatabase
import ci.nsu.mobile.main.data.entity.Deposit
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.Double
import kotlin.math.pow

data class DepositsUiState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val interestRate: String = "",
    val monthlyTopUp: String = "",
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,
    val availableInterestRate: List<Double> = listOf(),
){
    val isInitialAmountValid: Boolean get() = initialAmount.toDoubleOrNull() != null && initialAmount.toDouble() >= 0
    val isPeriodMonthsValid: Boolean get() = periodMonths.toIntOrNull() != null && periodMonths.toDouble() >= 0
    val isInterestRateValid: Boolean get() = interestRate.toDoubleOrNull() != null && interestRate.toDouble() >= 0
    val isMonthlyTopUpValid: Boolean get() = (monthlyTopUp.toDoubleOrNull() != null && monthlyTopUp.toDouble() >= 0) || monthlyTopUp == ""
    val isAllCorrect: Boolean get() = isInitialAmountValid && isPeriodMonthsValid && isInterestRateValid && isMonthlyTopUpValid
}

class DepositsViewModel(application: Application) : AndroidViewModel(application){
    private val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val depositDb = AppDatabase.getDatabase(application)
    val depositDbo = depositDb.depositDao()
    val repository: DepositRepository = DepositRepository(depositDbo)
    //++
    val allDepositList: StateFlow<List<Deposit>> = repository.depositList.asFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = repository.depositList.value ?: emptyList()
        )
    val allInterestRates = mapOf(1 to 0.15, 6 to 0.10, 12 to 0.05)
    private val _uiState = MutableStateFlow(DepositsUiState())
    val uiState: StateFlow<DepositsUiState> = _uiState.asStateFlow()

    fun setInitialAmount(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                initialAmount = newValue
            )
        }
    }

    fun setPeriodMonths(newValue: String){
        _uiState.update { currentState ->
            val newPeriodMonths = newValue.toDoubleOrNull()
            val newAvailableInterestRate: MutableList<Double> = mutableListOf()
            if(newPeriodMonths != null) allInterestRates.forEach { (key, value) -> if(key <= newPeriodMonths) newAvailableInterestRate.add(value)  }
            currentState.copy(
                periodMonths = newValue,
                availableInterestRate = newAvailableInterestRate,
                interestRate = ""
            )
        }
    }

    fun setInterestRate(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                interestRate = newValue
            )
        }
    }

    fun setMonthlyTopUp(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                monthlyTopUp = newValue
            )
        }
    }

    fun calcFinalAmountAndEarned(){
        _uiState.update { currentState ->
            var newFinalAmount = 0.0
            var newInterestEarned = 0.0
            if (currentState.isAllCorrect) {
                val initialAmount = currentState.initialAmount.toDouble()
                val interestRate = currentState.interestRate.toDouble()
                val periodMonths = currentState.periodMonths.toInt()
                val monthlyTopUp = currentState.monthlyTopUp.toDoubleOrNull()

                if (monthlyTopUp == null || monthlyTopUp == 0.0) {
                    newFinalAmount = initialAmount * (1 + interestRate/12).pow(periodMonths)
                    newInterestEarned = (newFinalAmount - initialAmount) / initialAmount
                } else {
                    newFinalAmount = initialAmount * (1 + interestRate/12).pow(periodMonths) +
                                     monthlyTopUp * (((1 + interestRate/12).pow(periodMonths) - 1) / (interestRate/12))
                    newInterestEarned = (newFinalAmount - initialAmount - monthlyTopUp * periodMonths) /
                                         initialAmount
                }
            }
            currentState.copy(
                finalAmount = newFinalAmount,
                interestEarned = newInterestEarned
            )
        }
    }

    fun saveDeposit(){
        _uiState.update { currentState ->
            if(currentState.isAllCorrect) {
                repository.addDeposit(
                    Deposit(
                        initialAmount = currentState.initialAmount.toDouble(),
                        periodMonths = currentState.periodMonths.toInt(),
                        interestRate = currentState.interestRate.toDouble(),
                        monthlyTopUp = currentState.monthlyTopUp.toDoubleOrNull(),
                        finalAmount = currentState.finalAmount,
                        interestEarned = currentState.interestEarned,
                        calculationDate = System.currentTimeMillis()
                    )
                )
                currentState.copy(
                    initialAmount = "",
                    periodMonths = "",
                    interestRate = "",
                    monthlyTopUp = "",
                    finalAmount = 0.0,
                    interestEarned = 0.0
                )
            } else currentState.copy()
        }
    }

    fun formatTime(timestamp: Long): String{
        return sdf.format(Date(timestamp))
    }
}