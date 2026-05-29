package ci.nsu.mobile.main.presenation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var initialAmount by mutableStateOf("")
    var months by mutableStateOf("")
    var monthlyTopUp by mutableStateOf("")

    var selectedRate by mutableStateOf(0.0)

    val history =
        repository.getAll()//получаем данные из репо
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(),
                emptyList()
            )

    fun calculate(): DepositCalculation {

        val initial =
            initialAmount.toDouble()
                //Получаем количество месяцев

        val period =
            months.toInt()

        val topUp =
            monthlyTopUp.toDoubleOrNull() ?: 0.0
        //Считаем пополнения за весь срок
        val totalTopUps =
            topUp * period
        //Добавляем пополнения к стартовому взносу
        val base =
            initial + totalTopUps
        //Начисляем проценты
        val interest =
            base * selectedRate / 100
        //Получаем итоговую сумму
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