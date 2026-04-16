package ci.nsu.mobile.main.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DepositRepository
    val calculations: LiveData<List<DepositCalculation>>
    init {
        val database = AppDatabase.getDatabase(application)
        repository = DepositRepository(database)
        calculations = repository.getAllCalculations().asLiveData()
    }
    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
fun <T> kotlinx.coroutines.flow.Flow<T>.asLiveData(): LiveData<T> {
    return androidx.lifecycle.asLiveData(viewModelScope.coroutineContext, this)
}