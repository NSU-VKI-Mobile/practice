package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SecondInputViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val KEY_TERM = "term"
    private val KEY_RATE = "rate"
    private val KEY_CURRENCY = "currency"
    private val KEY_INITIALIZED = "initialized"  // флаг, что начальные данные уже загружены
    private val KEY_START_AMOUNT = "startAmount"

    // Поля состояния
    private val _term = MutableStateFlow(savedStateHandle.get<String>(KEY_TERM) ?: "")
    val term: StateFlow<String> = _term.asStateFlow()

    private val _rate = MutableStateFlow(savedStateHandle.get<Double?>(KEY_RATE) ?: null)
    val rate: StateFlow<Double?> = _rate.asStateFlow()

    private val _currency =
        MutableStateFlow(savedStateHandle.get<String>(KEY_CURRENCY) ?: "Рубли (RUB)")
    val currency: StateFlow<String> = _currency.asStateFlow()

    private val _startAmount =
        MutableStateFlow(savedStateHandle.get<Double>(KEY_START_AMOUNT) ?: 0.0)
    val startAmount: StateFlow<Double> = _startAmount.asStateFlow()

    // Метод для первоначальной инициализации из Intent (вызывается один раз из Activity)
    fun initializeFromIntent(startAmount: Double, defaultTerm: Int) {
        // Проверяем, не были ли уже данные инициализированы (например, после поворота)
        val isInitialized = savedStateHandle.get<Boolean>(KEY_INITIALIZED) ?: false
        if (!isInitialized) {
            if (startAmount > 0) {
                updateStartAmount(startAmount)
            }
            if (defaultTerm > 0) {
                updateTerm(defaultTerm.toString())
            }
            savedStateHandle[KEY_INITIALIZED] = true
        }
    }

    fun updateTerm(value: String) {
        if (isValidTermMonths(value)) {
            _term.update { value }
            savedStateHandle[KEY_TERM] = value
        }
    }

    fun updateRate(value: Double?) {
        _rate.update { value }
        savedStateHandle[KEY_RATE] = value
    }

    fun updateCurrency(value: String) {
        _currency.update { value }
        savedStateHandle[KEY_CURRENCY] = value
    }

    fun updateStartAmount(value: Double) {
        _startAmount.update { value }
        savedStateHandle[KEY_START_AMOUNT] = value
    }

    private fun isValidTermMonths(value: String): Boolean {
        return value.isEmpty() || value.all { it.isDigit() }
    }

    // Геттеры для удобства (используются перед отправкой результата)
    fun getTermInt(): Int = _term.value.toIntOrNull() ?: 0
    fun getRateDouble(): Double = _rate.value ?: 0.0
}