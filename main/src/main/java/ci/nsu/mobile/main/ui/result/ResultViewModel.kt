package ci.nsu.mobile.main.ui.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.entity.CalculationResult
import ci.nsu.mobile.main.data.entity.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

sealed class SaveState {
    object Idle : SaveState()
    object Success : SaveState()
    data class Error(val message: String) : SaveState()
}

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository by lazy {
        val dao = AppDatabase.getInstance(application).depositDao()
        DepositRepository(dao)
    }

    private val _saveState = MutableLiveData<SaveState>(SaveState.Idle)
    val saveState: LiveData<SaveState> = _saveState

    // Флаг: расчёт уже сохранён — повторное сохранение запрещено
    private var alreadySaved = false

    fun saveCalculation(result: CalculationResult) {
        if (alreadySaved) return  // защита от повторного нажатия
        viewModelScope.launch {
            try {
                val entity = DepositCalculation(
                    initialAmount = result.initialAmount,
                    periodMonths = result.periodMonths,
                    interestRate = result.interestRate,
                    monthlyTopUp = result.monthlyTopUp,
                    finalAmount = result.finalAmount,
                    interestEarned = result.interestEarned,
                    calculationDate = System.currentTimeMillis()
                )
                repository.insert(entity)
                alreadySaved = true
                _saveState.value = SaveState.Success
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Ошибка сохранения")
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }

    val isSaved: Boolean get() = alreadySaved
}
