package ci.nsu.mobile.main.ui.screens.deposit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.local.SessionManager
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.roomDatabase.DepositEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val repository: DepositRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    init {
        Log.d("LOCAL", sessionManager.getUserId()!!)
    }

    var amount by mutableStateOf("")
    var months by mutableStateOf("")
    var rate by mutableStateOf(0.0)
    var monthlyTopUp by mutableStateOf("")

    var result by mutableStateOf<DepositEntity?>(null)

    val userId = sessionManager.getUserId()?.toInt()!!
    val history = repository.getDepositsByUserId(userId)
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
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
            userId = sessionManager.getUserId()?.toInt()!!,
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
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun formatDouble(value: Double): String {
        return String.format("%.2f", value)
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteDepositsByUserId(userId)
        }
    }

}