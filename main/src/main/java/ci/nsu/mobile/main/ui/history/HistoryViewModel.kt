package ci.nsu.mobile.main.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositDatabase
import ci.nsu.mobile.main.data.database.DepositEntity
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: DepositRepository
    var calculations: LiveData<List<DepositEntity>>

    init {
        val dao = DepositDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(dao)
        calculations = repository.getAllCalculations().asLiveData()
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun deleteCalculation(calculation: DepositEntity) {
        viewModelScope.launch {
            repository.delete(calculation)
        }
    }
    init {
        val dao = DepositDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(dao)
        calculations = repository.getAllCalculations().asLiveData()
        android.util.Log.d("HistoryViewModel", "ViewModel инициализирована")
    }
    fun restoreCalculation(entity: DepositEntity) {
        viewModelScope.launch {
            repository.saveCalculation(entity)
        }
    }
}