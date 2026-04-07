package ci.nsu.mobile.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.repository.DepositRepository

class DepositViewModelFactory(private val repository: DepositRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
            return DepositViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}