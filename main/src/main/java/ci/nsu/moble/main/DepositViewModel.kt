package ci.nsu.moble.main


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.AppDatabase
import ci.nsu.moble.main.data.DepositCalculation
import ci.nsu.moble.main.data.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(app: Application) : AndroidViewModel(app) {

    // Создаём Repository через Singleton базы данных
    private val repository = DepositRepository(
        AppDatabase.getDatabase(app).dao()
    )

    // ── Поля ввода Step 1 ────────────────────────────────
    var initialAmount by mutableStateOf("")

    var selectedRate  by mutableStateOf(15.0)

    // ── Поля ввода Step 2 ────────────────────────────────
    var periodMonths  by mutableStateOf("")
    var monthlyTopUp  by mutableStateOf("")

    // ── Результат расчёта ────────────────────────────────
    var result by mutableStateOf<DepositCalculation?>(null)

    // ── Детали выбранной записи из истории ───────────────
    var selectedDeposit by mutableStateOf<DepositCalculation?>(null)

    // ── История (Flow → StateFlow, читается в Compose) ───
    val history = repository.allDeposits.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )


    // Возвращает подсказку по сроку для выбранной ставки
    fun getPeriodHint(): String {
        return when (selectedRate) {
            15.0 -> "От 1 до 6 месяцев"
            10.0 -> "От 7 до 12 месяцев"
            else -> "От 13 месяцев и больше"
        }
    }

    fun isPeriodValid(): Boolean {
        val months = periodMonths.toIntOrNull() ?: return false
        return when (selectedRate) {
            15.0 -> months in 1..6
            10.0 -> months in 7..12
            else -> months >= 13
        }
    }

    // Расчёт вклада
    fun calculate() {
        val amount = initialAmount.toDoubleOrNull() ?: return
        val months = periodMonths.toIntOrNull() ?: return
        val topUp  = monthlyTopUp.toDoubleOrNull() ?: 0.0
        val rate   = selectedRate  // ← берём выбранную ставку

        var total = amount
        for (i in 1..months) {
            total += topUp
            total += total * (rate / 100.0 / 12.0)
        }
        val interest = total - amount - topUp * months

        result = DepositCalculation(
            initialAmount  = amount,
            periodMonths   = months,
            interestRate   = rate,
            monthlyTopUp   = topUp,
            finalAmount    = total,
            interestEarned = interest,
            date           = System.currentTimeMillis()
        )
    }

    // Сохранить результат через Repository
    fun save() {
        val calc = result ?: return
        viewModelScope.launch {
            repository.insert(calc)
        }
    }

    // Загрузить запись по ID для экрана деталей
    fun loadById(id: Long) {
        viewModelScope.launch {
            selectedDeposit = repository.getById(id)
        }
    }

    fun deleteById(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    // Сброс при новом расчёте
    fun reset() {
        initialAmount = ""
        periodMonths  = ""
        monthlyTopUp  = ""
        result        = null
    }
}