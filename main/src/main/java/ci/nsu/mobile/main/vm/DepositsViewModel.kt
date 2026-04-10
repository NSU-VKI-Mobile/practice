package ci.nsu.mobile.main.vm

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.AppDatabase
import ci.nsu.mobile.main.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow

data class DepositsUiState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null,
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0
)

class DepositsViewModel(context: Context) : ViewModel(){
    val depositDb = AppDatabase.getDatabase(context)
    val depositDbo = depositDb.depositDao()
    val repository: DepositRepository = DepositRepository(depositDbo)
    private val _uiState = MutableStateFlow(DepositsUiState())
    val uiState: StateFlow<DepositsUiState> = _uiState.asStateFlow()

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
}