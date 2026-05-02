package ci.nsu.mobile.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.domain.DepositCalculator
import ci.nsu.mobile.main.domain.DepositResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DepositFilterState(
    val minAmount: String = "",
    val maxAmount: String = "",
    val fromDate: String = "",
    val toDate: String = ""
)

data class DepositUiState(
    val userId: Long? = null,
    val isHistoryRefreshing: Boolean = false,
    val initialAmount: String = "",
    val periodMonths: String = "",
    val availableRates: List<Double> = emptyList(),
    val selectedRate: Double? = null,
    val monthlyTopUp: String = "",
    val filters: DepositFilterState = DepositFilterState(),
    val filterError: String? = null,
    val step1Error: String? = null,
    val step2Error: String? = null,
    val result: DepositResult? = null,
    val isSaved: Boolean = false,
    val saveMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    private val currentUserId = MutableStateFlow<Long?>(null)
    private val filters = MutableStateFlow(DepositFilterState())

    val history: StateFlow<List<DepositCalculation>> = combine(currentUserId, filters) { userId, filter ->
        userId to filter
    }.flatMapLatest { (userId, filter) ->
        if (userId == null) {
            flowOf(emptyList())
        } else {
            repository.getCalculationsForUser(
                userId = userId,
                minAmount = filter.minAmount.toMoneyOrNull(),
                maxAmount = filter.maxAmount.toMoneyOrNull(),
                fromDate = filter.fromDate.toStartOfDayMillisOrNull(),
                toDate = filter.toDate.toEndOfDayMillisOrNull()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun setUserId(userId: Long?) {
        currentUserId.value = userId
        _uiState.update { it.copy(userId = userId) }
    }

    fun getCalculationById(id: Long): Flow<DepositCalculation?> {
        val userId = currentUserId.value ?: return flowOf(null)
        return repository.getCalculationByIdForUser(id, userId)
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

    fun updateFilters(value: DepositFilterState) {
        val error = validateFilters(value)
        filters.value = if (error == null) value else DepositFilterState()
        _uiState.update { it.copy(filters = value, filterError = error) }
    }

    fun resetFilters() {
        filters.value = DepositFilterState()
        _uiState.update { it.copy(filters = DepositFilterState(), filterError = null) }
    }

    fun refreshHistory() {
        filters.value = _uiState.value.filters
        _uiState.update { it.copy(isHistoryRefreshing = true) }
        viewModelScope.launch {
            _uiState.update { it.copy(isHistoryRefreshing = false) }
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
        val userId = currentUserId.value
        if (userId == null) {
            _uiState.update { it.copy(saveMessage = "Не удалось определить текущего пользователя.") }
            return
        }

        if (_uiState.value.isSaved) {
            _uiState.update { it.copy(saveMessage = "Этот расчёт уже сохранён.") }
            return
        }

        viewModelScope.launch {
            repository.insertCalculation(
                DepositCalculation(
                    userId = userId,
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

    fun deleteCalculation(id: Long) {
        val userId = currentUserId.value ?: return
        viewModelScope.launch {
            repository.deleteCalculationByIdForUser(id, userId)
        }
    }

    fun clearDraft() {
        _uiState.update {
            DepositUiState(
                userId = currentUserId.value,
                filters = it.filters,
                filterError = it.filterError
            )
        }
    }

    private fun validateFilters(value: DepositFilterState): String? {
        val minAmount = value.minAmount.toMoneyOrNull()
        val maxAmount = value.maxAmount.toMoneyOrNull()
        return when {
            value.minAmount.isNotBlank() && minAmount == null -> "Минимальная сумма должна быть числом."
            value.maxAmount.isNotBlank() && maxAmount == null -> "Максимальная сумма должна быть числом."
            minAmount != null && maxAmount != null && minAmount > maxAmount -> "Минимальная сумма больше максимальной."
            value.fromDate.isNotBlank() && value.fromDate.toStartOfDayMillisOrNull() == null -> "Дата начала: формат dd.MM.yyyy."
            value.toDate.isNotBlank() && value.toDate.toEndOfDayMillisOrNull() == null -> "Дата конца: формат dd.MM.yyyy."
            else -> null
        }
    }

    private fun String.toMoneyOrNull(): Double? {
        return trim().replace(',', '.').takeIf { it.isNotBlank() }?.toDoubleOrNull()
    }

    private fun String.toStartOfDayMillisOrNull(): Long? {
        val date = parseDate() ?: return null
        return Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun String.toEndOfDayMillisOrNull(): Long? {
        val date = parseDate() ?: return null
        return Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    private fun String.parseDate(): Date? {
        val value = trim()
        if (value.isBlank()) return null
        return runCatching {
            SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru-RU")).apply {
                isLenient = false
            }.parse(value)
        }.getOrNull()
    }
}
