package ci.nsu.mobile.main.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.pow

class ResultViewModel(
    private val context: android.content.Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.depositDao()

    fun initialize(amount: Double, term: Int, rate: Double, monthlyAddition: Double) {
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
        if (_uiState.value.isSaved) {
            _uiState.update {
                it.copy(
                    errorMessage = "Расчет уже сохранен"
                )
            }
        }

        // Проверяем, не идет ли уже сохранение
        if (_uiState.value.isSaving) {
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true, errorMessage = null) }

                val calculation = DepositCalculationEntity(
                    initialAmount = _uiState.value.initialAmount,
                    periodMonths = _uiState.value.term,
                    interestRate = _uiState.value.rate,
                    monthlyTopUp = _uiState.value.monthlyAddition,
                    finalAmount = _uiState.value.finalAmount,
                    interestEarned = _uiState.value.earnedInterest,
                    calculationDate = System.currentTimeMillis()
                )
                dao.insert(calculation)
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