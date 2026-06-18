package ci.nsu.mobile.main.presentation.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.domain.repository.DepositRepository
import ci.nsu.mobile.main.domain.usecase.CalculateDepositUseCase

class ResultViewModelFactory(
    private val repository: DepositRepository,
    private val calculateDepositUseCase: CalculateDepositUseCase,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(repository, calculateDepositUseCase, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}