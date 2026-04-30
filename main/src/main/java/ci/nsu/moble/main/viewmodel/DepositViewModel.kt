package ci.nsu.moble.main.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.dao.DepositDao
import ci.nsu.moble.main.data.entities.DepositRecordEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(val dao: DepositDao) : ViewModel() {
    var amount by mutableStateOf("")
    var duration by mutableStateOf("")
    var monthlyAdd by mutableStateOf("")

    val history = dao.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getRate(): Double {
        val m = duration.toIntOrNull() ?: return 0.0
        return when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }
    }

    fun calculate(): DepositRecordEntity {
        val p = amount.toDoubleOrNull() ?: 0.0
        val n = duration.toIntOrNull() ?: 0
        val m = monthlyAdd.toDoubleOrNull() ?: 0.0
        val r = getRate() / 100 / 12

        var total = p
        repeat(n) { total = (total + m) * (1 + r) }

        return DepositRecordEntity(
            initialAmount = p, months = n, rate = getRate(),
            monthlyAdd = m, finalAmount = total, profit = total - p - (m * n)
        )
    }

    fun save(record: DepositRecordEntity) = viewModelScope.launch { dao.insert(record) }
    fun delete(record: DepositRecordEntity) = viewModelScope.launch { dao.delete(record) }
    fun deleteAll() = viewModelScope.launch { dao.deleteAll() }
}