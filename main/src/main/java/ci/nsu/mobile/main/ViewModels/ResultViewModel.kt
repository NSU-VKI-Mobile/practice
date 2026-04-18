package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResultViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val KEY_START_AMOUNT = "startAmount"
    private val KEY_TERM = "term"
    private val KEY_RATE = "rate"
    private val KEY_CURRENCY = "currency"
    private val KEY_INTEREST = "interest"
    private val KEY_TOTAL = "total"

    // Исходные данные
    private val _startAmount = MutableStateFlow(savedStateHandle.get<Double>(KEY_START_AMOUNT) ?: 0.0)
    val startAmount: StateFlow<Double> = _startAmount.asStateFlow()

    private val _term = MutableStateFlow(savedStateHandle.get<Int>(KEY_TERM) ?: 0)
    val term: StateFlow<Int> = _term.asStateFlow()

    private val _rate = MutableStateFlow(savedStateHandle.get<Double>(KEY_RATE) ?: 0.0)
    val rate: StateFlow<Double> = _rate.asStateFlow()

    private val _currency = MutableStateFlow(savedStateHandle.get<String>(KEY_CURRENCY) ?: "Рубли (RUB)")
    val currency: StateFlow<String> = _currency.asStateFlow()

    // Рассчитанные показатели
    private val _interest = MutableStateFlow(savedStateHandle.get<Double>(KEY_INTEREST) ?: 0.0)
    val interest: StateFlow<Double> = _interest.asStateFlow()

    private val _total = MutableStateFlow(savedStateHandle.get<Double>(KEY_TOTAL) ?: 0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    // Функция инициализации (вызывается из Activity с переданными из Intent данными)
    fun initializeFromIntent(startAmount: Double, term: Int, rate: Double, currency: String) {
        // Если данные уже были сохранены (например, после поворота), не перезаписываем
        val isInitialized = savedStateHandle.get<Boolean>("initialized") ?: false
        if (!isInitialized) {
            _startAmount.update { startAmount }
            _term.update { term }
            _rate.update { rate }
            _currency.update { currency }
            savedStateHandle[KEY_START_AMOUNT] = startAmount
            savedStateHandle[KEY_TERM] = term
            savedStateHandle[KEY_RATE] = rate
            savedStateHandle[KEY_CURRENCY] = currency

            // Расчёт процентов и итога
            val interestAmount = startAmount * (rate / 100.0) * (term / 12.0)
            val totalAmount = startAmount + interestAmount
            _interest.update { interestAmount }
            _total.update { totalAmount }
            savedStateHandle[KEY_INTEREST] = interestAmount
            savedStateHandle[KEY_TOTAL] = totalAmount
            savedStateHandle["initialized"] = true
        }
    }

    // Для отображения символа валюты
    fun getCurrencySymbol(): String = when (_currency.value) {
        "Рубли (RUB)" -> "₽"
        "Доллары (USD)" -> "$"
        "Евро (EUR)" -> "€"
        else -> ""
    }
}