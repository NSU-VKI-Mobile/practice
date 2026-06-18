package ci.nsu.mobile.main.presentation.screens.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.domain.usecase.CalculateDepositUseCase

class InputViewModelFactory(
    private val calculateDepositUseCase: CalculateDepositUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InputViewModel(calculateDepositUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}