package ci.nsu.mobile.main.ui.calculation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor(
    private val repository: DepositRepository
) : ViewModel() {
    var initialAmount by mutableStateOf("")
    var months by mutableStateOf("")
    var rate by mutableStateOf<Double?>(null)
    var monthlyTopUp by mutableStateOf("")

    fun getAvailableRate(): Double? {
        val m = months.toIntOrNull() ?: return null

        return when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }
    }

    fun calculateResult(): DepositEntity? {
        val amount = initialAmount.toDoubleOrNull() ?: return null
        val m = months.toIntOrNull() ?: return null
        val r = rate ?: return null
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val monthlyRate = r / 100 / 12
        var total = amount

        repeat(m) {
            total += total * monthlyRate
            total += topUp
        }

        val interest = total - amount - (topUp * m)

        return DepositEntity(
            initialAmount = amount,
            months = m,
            rate = r,
            monthlyTopUp = topUp,
            finalAmount = total,
            profit = interest,
            date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
        )
    }


    fun validateStep1(): String? {
        val amount = initialAmount.toDoubleOrNull()
        val m = months.toIntOrNull()

        return when {
            amount == null -> "Некорректный стартовый взнос"
            m == null -> "Некорректный срок"
            amount <= 0 -> "Сумма должна быть > 0"
            m <= 0 -> "Срок должен быть > 0"
            else -> null
        }
    }

    fun validateStep2(): String? {
        val m = months.toIntOrNull()
        val r = rate
        val topUp = monthlyTopUp.toDoubleOrNull()

        return when {
            m == null -> "Срок вклада не задан"
            r == null -> "Ставка не выбрана"
            monthlyTopUp.isNotBlank() && topUp == null -> "Некорректное пополнение"
            topUp != null && topUp < 0 -> "Пополнение не может быть < 0"
            else -> null
        }
    }

    fun save(deposit: DepositEntity?) {
        if (deposit != null) {
            viewModelScope.launch {
                repository.insert(deposit)
            }
        }
    }

}