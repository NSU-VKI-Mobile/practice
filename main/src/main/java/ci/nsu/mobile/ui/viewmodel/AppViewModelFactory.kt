package ci.nsu.mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.data.repository.DepositRepository

class AppViewModelFactory(
    private val depositRepository: DepositRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return DepositViewModel(depositRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}