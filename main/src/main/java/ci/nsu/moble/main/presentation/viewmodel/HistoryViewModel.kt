package ci.nsu.moble.main.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.AppDatabase
import ci.nsu.moble.main.data.DepositCalculation
import ci.nsu.moble.main.data.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    // База данных и репозиторий создаются внутри VM (просто для лабы)
    private val db = AppDatabase.getDatabase(application)
    private val repo = DepositRepository(db.depositDao())

    // Реактивный список расчётов
    val calculations: StateFlow<List<DepositCalculation>> = repo
        .getAllCalculations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}