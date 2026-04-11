package ci.nsu.mobile.main.vm

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.AppDatabase
import ci.nsu.mobile.main.DBO.Deposit
import ci.nsu.mobile.main.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.Double
import kotlin.math.pow

data class DepositsUiState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null,
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0
)

class DepositsViewModel(application: Application) : AndroidViewModel(application){
    val depositDb = AppDatabase.getDatabase(application)
    val depositDbo = depositDb.depositDao()
    val repository: DepositRepository = DepositRepository(depositDbo)
    private val _uiState = MutableStateFlow(DepositsUiState())
    val uiState: StateFlow<DepositsUiState> = _uiState.asStateFlow()

    fun SetInitialAmount(newValue: String){
        _uiState.update { currentState ->
            val newInitialAmount = if(newValue.toDoubleOrNull() != null) newValue.toDouble() else 0.0
            currentState.copy(
                initialAmount = newInitialAmount
            )
        }
    }

    fun SetPeriodMonths(newValue: String){
        _uiState.update { currentState ->
            val newPeriodMonths = if(newValue.toIntOrNull() != null) newValue.toInt() else 0
            currentState.copy(
                periodMonths = newPeriodMonths
            )
        }
    }

    fun SetInterestRate(newValue: String){
        _uiState.update { currentState ->
            val newInterestRate = if(newValue.toDoubleOrNull() != null) newValue.toDouble() else 0.0
            currentState.copy(
                interestRate = newInterestRate
            )
        }
    }

    fun SetMonthlyTopUp(newValue: String){
        _uiState.update { currentState ->
            val newMonthlyTopUp = newValue.toDoubleOrNull()
            currentState.copy(
                monthlyTopUp = newMonthlyTopUp
            )
        }
    }

    fun CalcFinalAmountAndEarned(){
        _uiState.update{currentState ->
            var newFinalAmount = 0.0
            var newInterestEarned = 0.0
            if(currentState.monthlyTopUp == null){
                newFinalAmount = currentState.initialAmount * (1 + currentState.interestRate).pow(currentState.periodMonths)
                newInterestEarned = (newFinalAmount - currentState.initialAmount) / currentState.initialAmount
            }
            else{
                newFinalAmount = currentState.initialAmount * (1 + currentState.interestRate).pow(currentState.periodMonths) + currentState.monthlyTopUp * (((1 + currentState.interestRate).pow(currentState.periodMonths) - 1) / currentState.interestRate)
                newInterestEarned = (newFinalAmount - (currentState.initialAmount + currentState.monthlyTopUp * currentState.periodMonths)) / (currentState.initialAmount + currentState.monthlyTopUp * currentState.periodMonths)
            }
            currentState.copy(
                finalAmount = newFinalAmount,
                interestEarned = newInterestEarned
            )
        }
    }

    fun SaveDeposit(){
        _uiState.update { currentState ->
            repository.AddDeposit(Deposit(
                initialAmount = currentState.initialAmount,
                periodMonths = currentState.periodMonths,
                interestRate = currentState.interestRate,
                monthlyTopUp = currentState.monthlyTopUp,
                finalAmount = currentState.finalAmount,
                interestEarned = currentState.interestEarned,
                calculationDate = System.currentTimeMillis()))
            currentState.copy(
                initialAmount = 0.0,
                periodMonths = 0,
                interestRate = 0.0,
                monthlyTopUp = null,
                finalAmount = 0.0,
                interestEarned = 0.0
            )
        }
    }
}