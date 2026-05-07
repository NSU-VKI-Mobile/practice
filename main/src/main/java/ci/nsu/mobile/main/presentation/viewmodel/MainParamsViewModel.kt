package ci.nsu.mobile.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainParamsViewModel : ViewModel() {

    // Храним текст полей. Почему String? Потому что пользователь может ввести "100." или пусто.
    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _months = MutableStateFlow("")
    val months: StateFlow<String> = _months.asStateFlow()

    // Ошибка для отображения красным текстом
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Обновление значений при вводе
    fun updateAmount(value: String) { _amount.value = value }
    fun updateMonths(value: String) { _months.value = value }

    // Главная логика: проверка и подготовка данных
    fun validateAndProceed(): MainParamsResult {
        val amountVal = _amount.value.toDoubleOrNull()
        val monthsVal = _months.value.toIntOrNull()

        return when {
            amountVal == null || amountVal <= 0 -> {
                _error.value = "Введите сумму больше 0"
                MainParamsResult.Error
            }
            monthsVal == null || monthsVal <= 0 -> {
                _error.value = "Введите срок больше 0 месяцев"
                MainParamsResult.Error
            }
            else -> {
                _error.value = null // Очищаем ошибку, если всё ок
                MainParamsResult.Success(amountVal, monthsVal)
            }
        }
    }

    // Результат проверки (Sealed class — как в HistoryViewModel)
    sealed class MainParamsResult {
        object Error : MainParamsResult()
        data class Success(val amount: Double, val months: Int) : MainParamsResult()
    }
}