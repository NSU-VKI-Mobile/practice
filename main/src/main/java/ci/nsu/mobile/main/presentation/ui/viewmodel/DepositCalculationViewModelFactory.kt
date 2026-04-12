package ci.nsu.mobile.main.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.domain.DepositRepository

class DepositCalculationViewModelFactory(
    private val repository: DepositRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepositCalculationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DepositCalculationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}