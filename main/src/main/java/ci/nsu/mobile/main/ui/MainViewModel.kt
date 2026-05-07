package ci.nsu.mobile.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.Deposit
import ci.nsu.mobile.main.data.DepositDatabase
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository

    val allDeposits: StateFlow<List<Deposit>>

    init {
        val dao = DepositDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(dao)
        allDeposits = repository.allDeposits.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun saveDeposit(deposit: Deposit) {
        viewModelScope.launch {
            repository.insert(deposit)
        }
    }

    fun getDepositById(id: Int): Flow<Deposit?> = repository.getDepositById(id)

    // НОВОЕ:
    fun clearHistory() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}