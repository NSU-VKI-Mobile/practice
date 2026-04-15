package ci.nsu.mobile.main.ui.result

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow

class ResultViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    fun initialize(amount: Double, term: Int, rate: Double, monthlyAddition: Double) {
        // Расчет сложного процента (синхронно, без корутин)
        val monthlyRate = rate / 100 / 12
        val months = term

        // Формула: итог = начальная сумма * (1 + ставка)^месяцы +
        //          ежемесячное пополнение * ((1 + ставка)^месяцы - 1) / ставка
        val compoundFactor = (1 + monthlyRate).pow(months)
        val finalFromInitial = amount * compoundFactor

        val finalFromMonthly = if (monthlyAddition > 0 && monthlyRate > 0) {
            monthlyAddition * (compoundFactor - 1) / monthlyRate
        } else {
            0.0
        }

        val finalAmount = finalFromInitial + finalFromMonthly
        val totalInvested = amount + (monthlyAddition * months)
        val earnedInterest = finalAmount - totalInvested

        _uiState.update {
            it.copy(
                initialAmount = amount,
                term = term,
                rate = rate,
                monthlyAddition = monthlyAddition,
                finalAmount = finalAmount,
                earnedInterest = earnedInterest,
                isLoading = false
            )
        }
    }
}