package ci.nsu.mobile.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.domain.DepositCalculator
import ci.nsu.mobile.main.domain.DepositResult
import ci.nsu.mobile.main.repository.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class DepositUiState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val availableRates: List<Double> = emptyList(),
    val selectedRate: Double? = null,
    val monthlyTopUp: String = "",
    val step1Error: String? = null,
    val step2Error: String? = null,
    val result: DepositResult? = null,
    val isSaved: Boolean = false,
    val saveMessage: String? = null
)

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    val history: StateFlow<List<DepositCalculation>> = repository.allCalculations.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun getCalculationById(id: Long): Flow<DepositCalculation?> {
        return repository.getCalculationById(id)
    }

    fun updateInitialAmount(value: String) {
        _uiState.update {
            it.copy(
                initialAmount = value,
                step1Error = null,
                result = null,
                isSaved = false,
                saveMessage = null
            )
        }
    }

    fun updatePeriodMonths(value: String) {
        val months = value.toIntOrNull()
        val rates = DepositCalculator.availableRates(months)

        _uiState.update {
            it.copy(
                periodMonths = value,
                availableRates = rates,
                selectedRate = rates.firstOrNull(),
                step1Error = null,
                step2Error = null,
                result = null,
                isSaved = false,
                saveMessage = null
            )
        }
    }

    fun updateSelectedRate(rate: Double) {
        _uiState.update {
            it.copy(
                selectedRate = rate,
                step2Error = null,
                result = null,
                isSaved = false,
                saveMessage = null
            )
        }
    }

    fun updateMonthlyTopUp(value: String) {
        _uiState.update {
            it.copy(
                monthlyTopUp = value,
                step2Error = null,
                result = null,
                isSaved = false,
                saveMessage = null
            )
        }
    }

    fun validateStep1(): Boolean {
        val state = _uiState.value
        val amount = state.initialAmount.toMoneyOrNull()
        val months = state.periodMonths.toIntOrNull()

        val error = when {
            state.initialAmount.isBlank() -> "Укажите стартовый взнос."
            amount == null || amount <= 0.0 -> "Стартовый взнос должен быть положительным числом."
            state.periodMonths.isBlank() -> "Укажите срок вклада в месяцах."
            months == null || months <= 0 -> "Срок вклада должен быть целым числом больше нуля."
            else -> null
        }

        if (error != null) {
            _uiState.update { it.copy(step1Error = error) }
            return false
        }

        val rates = DepositCalculator.availableRates(months)
        _uiState.update {
            it.copy(
                availableRates = rates,
                selectedRate = rates.firstOrNull(),
                step1Error = null
            )
        }
        return true
    }

    fun calculateResult(): Boolean {
        val state = _uiState.value
        val amount = state.initialAmount.toMoneyOrNull()
        val months = state.periodMonths.toIntOrNull()
        val rate = state.selectedRate
        val topUp = state.monthlyTopUp.takeIf { it.isNotBlank() }?.toMoneyOrNull()

        val error = when {
            amount == null || amount <= 0.0 -> "Вернитесь на первый этап и укажите корректный взнос."
            months == null || months <= 0 -> "Вернитесь на первый этап и укажите корректный срок."
            state.availableRates.isEmpty() -> "Сначала укажите корректный срок вклада."
            rate == null -> "Выберите процентную ставку."
            state.monthlyTopUp.isNotBlank() && topUp == null -> "Пополнение должно быть числом."
            topUp != null && topUp < 0.0 -> "Пополнение не может быть отрицательным."
            else -> null
        }

        if (error != null) {
            _uiState.update { it.copy(step2Error = error) }
            return false
        }

        val result = DepositCalculator.calculate(
            initialAmount = amount!!,
            periodMonths = months!!,
            interestRate = rate!!,
            monthlyTopUp = topUp
        )

        _uiState.update {
            it.copy(
                result = result,
                step2Error = null,
                isSaved = false,
                saveMessage = null
            )
        }
        return true
    }

    fun saveCurrentResult() {
        val result = _uiState.value.result ?: return

        if (_uiState.value.isSaved) {
            _uiState.update { it.copy(saveMessage = "Этот расчёт уже сохранён.") }
            return
        }

        viewModelScope.launch {
            repository.insertCalculation(
                DepositCalculation(
                    initialAmount = result.initialAmount,
                    periodMonths = result.periodMonths,
                    interestRate = result.interestRate,
                    monthlyTopUp = result.monthlyTopUp,
                    finalAmount = result.finalAmount,
                    interestEarned = result.interestEarned,
                    calculationDate = System.currentTimeMillis()
                )
            )
            _uiState.update {
                it.copy(
                    isSaved = true,
                    saveMessage = "Расчёт сохранён в историю."
                )
            }
        }
    }

    fun clearDraft() {
        _uiState.value = DepositUiState()
    }

    private fun String.toMoneyOrNull(): Double? {
        return trim().replace(',', '.').toDoubleOrNull()
    }
}

class DepositViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val appContext = context.applicationContext

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
            val dao = AppDatabase.getDatabase(appContext).depositDao()
            val repository = DepositRepository(dao)

            @Suppress("UNCHECKED_CAST")
            return DepositViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}