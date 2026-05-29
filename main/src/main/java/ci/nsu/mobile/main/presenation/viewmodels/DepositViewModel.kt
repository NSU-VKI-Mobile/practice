package ci.nsu.mobile.main.presenation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.presenation.repositories.DepositRepository
import ci.nsu.mobile.main.data.entities.DepositCalculation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository
) : ViewModel() {

    var initialAmount = ""
    var months = ""
    var monthlyTopUp = ""

    var selectedRate = 0.0

    val history =
        repository.getAll()
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(),
                emptyList()
            )

    fun calculate(): DepositCalculation {

        val initial =
            initialAmount.toDouble()

        val period =
            months.toInt()

        val topUp =
            monthlyTopUp.toDoubleOrNull() ?: 0.0

        val totalTopUps =
            topUp * period

        val base =
            initial + totalTopUps

        val interest =
            base * selectedRate / 100

        val final =
            base + interest

        return DepositCalculation(

            initialAmount = initial,

            periodMonths = period,

            interestRate = selectedRate,

            monthlyTopUp = topUp,

            finalAmount = final,

            interestEarned = interest,

            calculationDate = System.currentTimeMillis()
        )
    }

    fun save(calc: DepositCalculation) {

        viewModelScope.launch {

            repository.insert(calc)
        }
    }
}