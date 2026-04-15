package ci.nsu.mobile.main.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.dao.DepositDao
import ci.nsu.mobile.main.entity.DepositEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted


class MainViewModel(private val dao: DepositDao) : ViewModel() {

    var startAmount by mutableStateOf("")
    var months by mutableStateOf("")
    var rate by mutableStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")

    var result by mutableStateOf<DepositEntity?>(null)
    val history: StateFlow<List<DepositEntity>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun calculateDeposit() {
        val start = startAmount.toDoubleOrNull() ?: return
        val m = months.toIntOrNull() ?: return
        val topUp = monthlyTopUp.toDoubleOrNull() ?: 0.0

        var total = start
        repeat(m) {
            total += topUp
            total += total * (rate / 100)
        }

        val profit = total - (start + topUp * m)

        result = DepositEntity(
            startAmount = start,
            months = m,
            rate = rate,
            monthlyTopUp = topUp,
            finalAmount = total,
            profit = profit,
            date = System.currentTimeMillis()
        )
    }

    fun saveDeposit() {
        result?.let {
            viewModelScope.launch { dao.insert(it) }
        }
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