package ci.nsu.moble.main.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.DepositCalculationEntity
import ci.nsu.moble.main.data.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.round

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DepositUiState())
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    fun updateInitialAmount(value: String) {
        _uiState.update {
            it.copy(
                initialAmount = value,
                errorMessage = null,
                saveMessage = null
            )
        }
    }

    fun updateMonths(value: String) {
        _uiState.update {
            it.copy(
                months = value,
                errorMessage = null,
                saveMessage = null
            )
        }
    }

    fun updateMonthlyTopUp(value: String) {
        _uiState.update {
            it.copy(
                monthlyTopUp = value,
                errorMessage = null,
                saveMessage = null
            )
        }
    }

    fun validateStepOne(): Boolean {
        val initial = uiState.value.initialAmount.toDoubleOrNull()
        val months = uiState.value.months.toIntOrNull()

        return when {
            uiState.value.initialAmount.isBlank() -> {
                setError("Введите стартовый взнос")
                false
            }
            initial == null || initial <= 0.0 -> {
                setError("Стартовый взнос должен быть больше 0")
                false
            }
            uiState.value.months.isBlank() -> {
                setError("Введите срок вклада")
                false
            }
            months == null || months <= 0 -> {
                setError("Срок вклада должен быть целым числом больше 0")
                false
            }
            else -> {
                clearError()
                updateRateByMonths()
                true
            }
        }
    }

    fun validateStepTwo(): Boolean {
        val monthlyTopUpText = uiState.value.monthlyTopUp
        val monthlyTopUp = monthlyTopUpText.toDoubleOrNull()

        return when {
            monthlyTopUpText.isNotBlank() && (monthlyTopUp == null || monthlyTopUp < 0.0) -> {
                setError("Ежемесячное пополнение должно быть числом не меньше 0")
                false
            }
            else -> {
                clearError()
                true
            }
        }
    }

    private fun updateRateByMonths() {
        val months = uiState.value.months.toIntOrNull() ?: return
        val rate = resolveRate(months)
        _uiState.update { it.copy(ratePercent = rate) }
    }

    private fun resolveRate(months: Int): Int {
        return when {
            months < 6 -> 15
            months < 12 -> 10
            else -> 5
        }
    }

    fun calculateDeposit() {
        val initial = uiState.value.initialAmount.toDoubleOrNull() ?: return
        val months = uiState.value.months.toIntOrNull() ?: return
        val topUp = uiState.value.monthlyTopUp.toDoubleOrNull() ?: 0.0

        if (initial == null || initial <= 0.0) {
            setError("Введите корректный стартовый взнос")
            return
        }

        if (months == null || months <= 0) {
            setError("Введите корректный срок вклада")
            return
        }

        val rate = resolveRate(months)
        val monthlyRate = rate / 100.0 / 12.0

        var total = initial
        repeat(months) {
            total += topUp
            total += total * monthlyRate
        }

        val interest = total - initial - topUp * months

        _uiState.update {
            it.copy(
                ratePercent = rate,
                finalAmount = round2(total),
                interestAmount = round2(interest),
                errorMessage = null
            )
        }
    }

    fun saveCalculation() {
        val initial = uiState.value.initialAmount.toDoubleOrNull() ?: return
        val months = uiState.value.months.toIntOrNull() ?: return
        val rate = uiState.value.ratePercent ?: return
        val topUp = uiState.value.monthlyTopUp.toDoubleOrNull() ?: 0.0
        val finalAmount = uiState.value.finalAmount ?: return
        val interest = uiState.value.interestAmount ?: return

        viewModelScope.launch {
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val now = formatter.format(Date())

            repository.saveCalculation(
                DepositCalculationEntity(
                    dateTime = now,
                    initialAmount = initial,
                    months = months,
                    ratePercent = rate,
                    monthlyTopUp = topUp,
                    finalAmount = finalAmount,
                    interestAmount = interest
                )
            )

            _uiState.update {
                it.copy(saveMessage = "Расчёт сохранён")
            }
        }
    }

    fun resetAll() {
        _uiState.value = DepositUiState()
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                errorMessage = null,
                saveMessage = null
            )
        }
    }

    private fun setError(message: String) {
        _uiState.update { it.copy(errorMessage = message) }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun round2(value: Double): Double {
        return round(value * 100) / 100
    }

    class Factory(
        private val repository: DepositRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
                return DepositViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}