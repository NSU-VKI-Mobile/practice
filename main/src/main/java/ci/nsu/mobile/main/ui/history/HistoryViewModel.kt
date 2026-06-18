package ci.nsu.mobile.main.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.entity.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository by lazy {
        val dao = AppDatabase.getInstance(application).depositDao()
        DepositRepository(dao)
    }

    val calculations: LiveData<List<DepositCalculation>> = repository.getAllCalculations()

    fun clearAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}
