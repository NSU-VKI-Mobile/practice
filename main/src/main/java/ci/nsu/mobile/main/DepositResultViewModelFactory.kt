package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.DepositRepository

class DepositResultViewModelFactory(
    private val repository: DepositRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepositResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DepositResultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
