package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.db.DepositCalculationEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DepositResultViewModel(application: Application) : AndroidViewModel(application) {

    data class UiState(
        val initialAmount: Double? = null,
        val periodMonths: Int? = null,
        val interestRate: Double? = null,
        val monthlyTopUp: Double? = null,
        val finalAmount: Double? = null,
        val interestEarned: Double? = null,
    )

    private val repository: DepositRepository by lazy {
        val db = AppDatabase.getDatabase(getApplication())
        DepositRepository(db.depositCalculationDao())
    }

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _saveResultToast = MutableLiveData<Event<String>>()
    val saveResultToast: LiveData<Event<String>> = _saveResultToast

    private val _navigationToHistory = MutableLiveData<Event<Unit>>()
    val navigationToHistory: LiveData<Event<Unit>> = _navigationToHistory

    private var initialized = false

    fun setResultOnce(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?,
        finalAmount: Double,
        interestEarned: Double,
    ) {
        if (initialized) return
        initialized = true
        _uiState.value = UiState(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned
        )
    }

    fun onSaveClicked() {
        val current = _uiState.value ?: UiState()
        val initialAmount = current.initialAmount ?: return
        val periodMonths = current.periodMonths ?: return
        val interestRate = current.interestRate ?: return
        val finalAmount = current.finalAmount ?: return
        val interestEarned = current.interestEarned ?: return

        val monthlyTopUp = current.monthlyTopUp

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entity = DepositCalculationEntity(
                    initialAmount = initialAmount,
                    periodMonths = periodMonths,
                    interestRate = interestRate,
                    monthlyTopUp = monthlyTopUp,
                    finalAmount = finalAmount,
                    interestEarned = interestEarned,
                    calculationDate = System.currentTimeMillis()
                )
                repository.insertCalculation(entity)
                _saveResultToast.postValue(Event("Расчёт сохранён в историю"))
                _navigationToHistory.postValue(Event(Unit))
            } catch (t: Throwable) {
                _saveResultToast.postValue(Event("Ошибка сохранения: ${t.message ?: "unknown"}"))
            }
        }
    }
}

