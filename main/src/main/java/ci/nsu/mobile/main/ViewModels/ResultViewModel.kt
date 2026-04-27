package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Data.Entity.CalculationEntity
import ci.nsu.mobile.main.Data.Repository.CalculationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val savedStateHandle: SavedStateHandle, private val repository: CalculationRepository
) : ViewModel() {

    private val KEY_START = "startAmount"
    private val KEY_TERM = "term"
    private val KEY_RATE = "rate"
    private val KEY_CURRENCY = "currency"
    private val KEY_INTEREST = "interest"
    private val KEY_TOTAL = "total"
    private val KEY_INITIALIZED = "initialized"

    private val _startAmount = MutableStateFlow(savedStateHandle.get<Double>(KEY_START) ?: 0.0)
    val startAmount: StateFlow<Double> = _startAmount.asStateFlow()

    private val _term = MutableStateFlow(savedStateHandle.get<Int>(KEY_TERM) ?: 0)
    val term: StateFlow<Int> = _term.asStateFlow()

    private val _rate = MutableStateFlow(savedStateHandle.get<Double>(KEY_RATE) ?: 0.0)
    val rate: StateFlow<Double> = _rate.asStateFlow()

    private val _currency =
        MutableStateFlow(savedStateHandle.get<String>(KEY_CURRENCY) ?: "Рубли (RUB)")
    val currency: StateFlow<String> = _currency.asStateFlow()

    private val _interest = MutableStateFlow(savedStateHandle.get<Double>(KEY_INTEREST) ?: 0.0)
    val interest: StateFlow<Double> = _interest.asStateFlow()

    private val _total = MutableStateFlow(savedStateHandle.get<Double>(KEY_TOTAL) ?: 0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    fun initializeFromIntent(startAmount: Double, term: Int, rate: Double, currency: String) {
        val isInitialized = savedStateHandle.get<Boolean>(KEY_INITIALIZED) ?: false
        if (!isInitialized) {
            _startAmount.value = startAmount
            _term.value = term
            _rate.value = rate
            _currency.value = currency
            savedStateHandle[KEY_START] = startAmount
            savedStateHandle[KEY_TERM] = term
            savedStateHandle[KEY_RATE] = rate
            savedStateHandle[KEY_CURRENCY] = currency

            val interestAmount = startAmount * (rate / 100.0) * (term / 12.0)
            val totalAmount = startAmount + interestAmount
            _interest.value = interestAmount
            _total.value = totalAmount
            savedStateHandle[KEY_INTEREST] = interestAmount
            savedStateHandle[KEY_TOTAL] = totalAmount
            savedStateHandle[KEY_INITIALIZED] = true
        }
    }

    fun saveCalculation() {
        viewModelScope.launch {
            val calculation = CalculationEntity(
                startAmount = _startAmount.value,
                termMonths = _term.value,
                rate = _rate.value,
                currency = _currency.value,
                interest = _interest.value,
                total = _total.value
            )
            repository.insertCalculation(calculation)
            _saveSuccess.value = true
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }

    fun getCurrencySymbol(): String = when (_currency.value) {
        "Рубли (RUB)" -> "₽"
        "Доллары (USD)" -> "$"
        "Евро (EUR)" -> "€"
        else -> ""
    }
}