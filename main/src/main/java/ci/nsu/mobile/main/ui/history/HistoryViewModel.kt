package ci.nsu.mobile.main.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository
    private val _calculations = MutableLiveData<List<DepositCalculation>>()
    val calculations: LiveData<List<DepositCalculation>> = _calculations

    init {
        val database = AppDatabase.getDatabase(application)
        repository = DepositRepository(database)
        loadCalculations()
    }

    private fun loadCalculations() {
        viewModelScope.launch {
            try {
                val list = repository.getAllCalculations()
                _calculations.postValue(list)
            } catch (e: Exception) {
                _calculations.postValue(emptyList())
                e.printStackTrace()
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                repository.clearHistory()
                loadCalculations()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}