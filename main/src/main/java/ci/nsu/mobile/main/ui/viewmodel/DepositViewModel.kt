package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repo: DepositRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    private val userId = authRepo.getUserId() ?: 0L

    val calculations: StateFlow<List<DepositCalculation>> =
        repo.getCalculations(userId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _calcResult = MutableStateFlow<DepositCalculation?>(null)
    val calcResult: StateFlow<DepositCalculation?> = _calcResult.asStateFlow()

    fun calculateDeposit(amount: Double, months: Int, rate: Double, topUp: Double) {
        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            val interest = total * monthlyRate
            earned += interest
            total += interest + topUp
        }

        val calculation = DepositCalculation(
            userId = userId,
            initialAmount = amount,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = total,
            interestEarned = earned,
            calculationDate = System.currentTimeMillis()
        )
        _calcResult.value = calculation
    }

    fun saveCalculation(calc: DepositCalculation) {
        viewModelScope.launch {
            repo.saveCalculation(calc)
            _calcResult.value = null
        }
    }

    fun deleteCalculation(calc: DepositCalculation) {
        viewModelScope.launch {
            repo.deleteCalculation(calc)
        }
    }
}