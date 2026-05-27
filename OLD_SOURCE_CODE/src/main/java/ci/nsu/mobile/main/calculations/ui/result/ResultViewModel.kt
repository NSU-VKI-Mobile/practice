package ci.nsu.mobile.main.calculations.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.calculations.data.repository.DepositRepository
import ci.nsu.mobile.main.calculations.data.database.DepositCalculationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val depositRepository: DepositRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        // Get arguments from navigation
        val amount = savedStateHandle.get<Double>("amount") ?: 0.0
        val term = savedStateHandle.get<Int>("term") ?: 0
        val rate = savedStateHandle.get<Double>("rate") ?: 0.0
        val monthlyAddition = savedStateHandle.get<Double>("monthlyAddition") ?: 0.0

        calculateResult(amount, term, rate, monthlyAddition)
    }

    private fun calculateResult(amount: Double, term: Int, rate: Double, monthlyAddition: Double) {
        val monthlyRate = rate / 100 / 12
        // Формула: итог = начальная сумма * (1 + ставка)^месяцы +
        //          ежемесячное пополнение * ((1 + ставка)^месяцы - 1) / ставка
        val compoundFactor = (1 + monthlyRate).pow(term)
        val finalFromInitial = amount * compoundFactor

        val finalFromMonthly = if (monthlyAddition > 0 && monthlyRate > 0) {
            monthlyAddition * (compoundFactor - 1) / monthlyRate
        } else {
            0.0
        }

        val finalAmount = finalFromInitial + finalFromMonthly
        val totalInvested = amount + (monthlyAddition * term)
        val earnedInterest = finalAmount - totalInvested

        _uiState.update {
            it.copy(
                initialAmount = amount,
                term = term,
                rate = rate,
                monthlyAddition = monthlyAddition,
                finalAmount = finalAmount,
                earnedInterest = earnedInterest,
                isSaved = false,
                isSaving = false,
                isLoading = false
            )
        }
    }

    fun saveCalculationToDatabase() {
        // Проверяем, не сохранили ли уже
        if (_uiState.value.isSaved || _uiState.value.isSaving) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true, errorMessage = null) }

                val currentState = _uiState.value
                depositRepository.saveDepositForCurrentUser(
                    initialAmount = currentState.initialAmount,
                    periodMonths = currentState.term,
                    interestRate = currentState.rate,
                    monthlyTopUp = currentState.monthlyAddition,
                    finalAmount = currentState.finalAmount,
                    interestEarned = currentState.earnedInterest,
                )
                _uiState.update { it.copy(isSaved = true, isSaving = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Ошибка сохранения"
                    )
                }
            }
        }
    }

    fun resetSavedFlag() {
        _uiState.update { it.copy(isSaved = false) }
    }
}