package ci.nsu.mobile.main.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.roomDatabase.DepositEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    var amount by mutableStateOf("")
    var months by mutableStateOf("")
    var rate by mutableStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")

    var result by mutableStateOf<DepositEntity?>(null)

    val history = repository.getAll().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun calculate() {
        val a = amount.toDoubleOrNull() ?: return
        val m = months.toIntOrNull() ?: return
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        val monthlyRate = rate / 100 / 12
        var total = a

        repeat(m) {
            total += topUp
            total += total * monthlyRate
        }

        val profit = total - (a + topUp * m)

        result = DepositEntity(
            amount = a,
            months = m,
            rate = rate,
            monthlyTopUp = topUp,
            finalAmount = total,
            profit = profit,
            date = System.currentTimeMillis()
        )
    }

    fun save() {
        viewModelScope.launch {
            result?.let { repository.insert(it) }
        }
    }

    fun determineRate(): List<Double> {
        val m = months.toIntOrNull() ?: return emptyList()

        return when {
            m < 6 -> listOf(15.0)
            m < 12 -> listOf(10.0)
            else -> listOf(5.0)
        }
    }

    fun reset() {
        amount = ""
        months = ""
        rate = 0.0
        monthlyTopUp = ""

        result = null
    }

    fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }

    fun formatDouble(value: Double): String {
        return String.format("%.2f", value)
    }
}