package ci.nsu.mobile.main.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAmount

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DepositRepository
    init {
        val database = AppDatabase.getDatabase(application)
        repository = DepositRepository(database)
    }
    fun saveCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?,
        finalAmount: Double,
        interestEarned: Double,
        timestamp: Long
    ) {
        viewModelScope.launch {
            val calculation = DepositCalculation(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned,
                calculationDate = timestamp
            )
            repository.insertCalculation(calculation)
        }
    }
}