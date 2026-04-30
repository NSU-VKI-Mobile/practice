package ci.nsu.moble.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DepositViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).depositDao()
    val history = dao.getAll()

    var amount: Double = 0.0
    var months: Int = 0
    var monthlyAdd: Double = 0.0
    var rate: Double = 0.0

    fun calculate(): DepositCalculation {
        var total = amount
        for (i in 1..months) {
            total += monthlyAdd
            total *= (1 + (rate / 100 / 12))
        }
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return DepositCalculation(
            initialAmount = amount,
            months = months,
            rate = rate,
            monthlyAdd = monthlyAdd,
            finalAmount = total,
            profit = total - amount - (monthlyAdd * months),
            date = sdf.format(Date())
        )
    }

    fun save(calc: DepositCalculation) {
        viewModelScope.launch { dao.insert(calc) }
    }
}
