package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.db.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    var initialAmount: Double? = null
    var periodMonths: Int? = null
    var interestRate: Double = 0.0
    var monthlyTopUp: Double? = null

    private val _result = MutableStateFlow<DepositCalculation?>(null)
    val result: StateFlow<DepositCalculation?> = _result

    fun calculate() {
        val start = initialAmount ?: 0.0
        val months = periodMonths ?: 0
        val rate = interestRate / 100.0
        val topUp = monthlyTopUp ?: 0.0

        val totalTopUps = topUp * months
        val baseAmount = start + totalTopUps
        val interestEarned = baseAmount * rate * (months / 12.0)
        val finalAmount = baseAmount + interestEarned

        _result.value = DepositCalculation(
            initialAmount = start,
            periodMonths = months,
            interestRate = interestRate,
            monthlyTopUp = if (monthlyTopUp == null) null else topUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )
    }

    suspend fun saveCalculation() {
        _result.value?.let { repository.insert(it) }
    }

    fun isStep1Valid(): Boolean {
        return initialAmount != null && initialAmount!! > 0 &&
                periodMonths != null && periodMonths!! > 0
    }

    fun getAvailableRates(): List<Double> {
        val months = periodMonths ?: return emptyList()
        return when {
            months < 6 -> listOf(15.0)
            months in 6..11 -> listOf(10.0)
            else -> listOf(5.0)
        }
    }

    class Factory(private val repository: DepositRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
                return DepositViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}