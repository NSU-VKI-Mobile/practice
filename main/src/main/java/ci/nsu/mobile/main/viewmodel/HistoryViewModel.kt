package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(repository: DepositRepository) : ViewModel() {
    val history = repository.history.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )
}