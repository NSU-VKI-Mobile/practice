package ci.nsu.mobile.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdditionalParamsViewModel : ViewModel() {

    // Ежемесячное пополнение
    private val _monthlyTopUp = MutableStateFlow("")
    val monthlyTopUp: StateFlow<String> = _monthlyTopUp.asStateFlow()

    // Ошибка
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Сюда передадим данные из MainParamsViewModel
    var mainParams: MainParamsViewModel.MainParamsResult.Success? = null

    // Автоматическая ставка
    val interestRate: Double
        get() = when {
            mainParams == null -> 0.0
            mainParams!!.months < 6 -> 15.0
            mainParams!!.months < 12 -> 10.0
            else -> 5.0
        }

    fun updateTopUp(value: String) { _monthlyTopUp.value = value }

    fun validate(): AdditionalParamsResult {
        val topUpVal = _monthlyTopUp.value.toDoubleOrNull() ?: 0.0
        if (topUpVal < 0) {
            _error.value = "Пополнение не может быть отрицательным"
            return AdditionalParamsResult.Error
        }
        _error.value = null
        return AdditionalParamsResult.Success(topUpVal, interestRate)
    }

    sealed class AdditionalParamsResult {
        object Error : AdditionalParamsResult()
        data class Success(val topUp: Double, val rate: Double) : AdditionalParamsResult()
    }
}