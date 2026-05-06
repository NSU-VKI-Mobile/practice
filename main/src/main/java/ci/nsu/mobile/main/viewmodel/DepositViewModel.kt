package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ci.nsu.mobile.main.data.*
import ci.nsu.mobile.main.repository.DepositRepository
import kotlinx.coroutines.launch

class DepositViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.get(application).dao()

    val history: LiveData<List<DepositCalculation>> = dao.getAll()

    fun save(item: DepositCalculation) {
        viewModelScope.launch {
            dao.insert(item)
        }
    }

    fun delete(item: DepositCalculation) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}