package ci.nsu.mobile.main.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositDatabase
import ci.nsu.mobile.main.data.database.DepositEntity
import ci.nsu.mobile.main.data.model.DepositResultData
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DepositDatabase.getDatabase(application).depositDao()

    private val _resultData = MutableLiveData<DepositResultData>()
    val resultData: LiveData<DepositResultData> = _resultData

    private val _isSaved = MutableLiveData(false)
    val isSaved: LiveData<Boolean> = _isSaved

    fun calculateAndShow(data: DepositResultData) {
        val monthlyRate = data.interestRate / 100 / 12
        var amount = data.initialAmount
        var totalInterest = 0.0
        val monthlyTopUp = data.monthlyTopUp ?: 0.0

        for (month in 1..data.periodMonths) {
            val interest = amount * monthlyRate
            totalInterest += interest
            amount += interest + monthlyTopUp
        }

        data.finalAmount = (amount * 100).toInt() / 100.0
        data.interestEarned = (totalInterest * 100).toInt() / 100.0
        _resultData.value = data
        _isSaved.value = false  // Сбрасываем флаг при новом расчёте
    }

    fun saveCalculation(data: DepositResultData) {
        viewModelScope.launch {
            val entity = DepositEntity(
                initialAmount = data.initialAmount,
                periodMonths = data.periodMonths,
                interestRate = data.interestRate,
                monthlyTopUp = data.monthlyTopUp,
                finalAmount = data.finalAmount,
                interestEarned = data.interestEarned,
                calculationDate = System.currentTimeMillis()
            )
            repository.insert(entity)
            _isSaved.postValue(true)  // Блокируем кнопку
        }
    }

}