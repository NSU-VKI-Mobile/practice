package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.Flow

class DepositDetailsViewModel(private val repository: DepositRepository) : ViewModel() {

    fun getCalculationById(id: Long): Flow<DepositCalculation?> {
        return repository.getCalculationById(id)
    }
}
