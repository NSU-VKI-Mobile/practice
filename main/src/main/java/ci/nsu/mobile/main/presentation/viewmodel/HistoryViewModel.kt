package ci.nsu.mobile.main.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import ci.nsu.mobile.main.data.repository.DepositRepository

class HistoryViewModel(repo: DepositRepository) : ViewModel() {
    val history = repo.all.asLiveData()
}
