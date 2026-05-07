package ci.nsu.mobile.main.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.dao.DepositDao
import ci.nsu.mobile.main.entity.DepositEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DepositViewModel(
    private val dao: DepositDao, private val userId: Long
) : ViewModel() {
    var startAmount by mutableStateOf("")
    var months by mutableStateOf("")
    var rate by mutableStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")

    var currentStep by mutableStateOf(0)

    var result by mutableStateOf<DepositEntity?>(null)
    var savedMessage by mutableStateOf<String?>(null)

    val history: StateFlow<List<DepositEntity>> =
        dao.getByUser(userId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun calculateDeposit() {
        val start = startAmount.toDoubleOrNull() ?: return
        val m = months.toIntOrNull() ?: return
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        var total = start
        repeat(m) { total = (total + topUp) * (1 + rate / 100) }

        result = DepositEntity(
            userId = userId,
            startAmount = start,
            months = m,
            rate = rate,
            monthlyTopUp = topUp,
            finalAmount = total,
            profit = total - (start + topUp * m),
            date = System.currentTimeMillis()
        )
        currentStep = 2
    }

    fun saveDeposit() {
        result?.let { entity ->
            viewModelScope.launch {
                dao.insert(entity)
                savedMessage = "Расчёт сохранён"
            }
        }
    }

    fun deleteDeposit(entity: DepositEntity) {
        viewModelScope.launch { dao.delete(entity) }
    }

    fun resetCalculation() {
        startAmount = ""; months = ""; rate = 0.0; monthlyTopUp = ""
        result = null; currentStep = 0; savedMessage = null
    }
    fun goToStep(step: Int) {
        currentStep = step
    }

    fun resolveRate(): List<Double> {
        val m = months.toIntOrNull() ?: return emptyList()
        return when {
            m < 6 -> listOf(15.0)
            m < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
    }
}