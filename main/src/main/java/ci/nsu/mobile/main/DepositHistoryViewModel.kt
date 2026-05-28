package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import ci.nsu.mobile.main.data.DepositCalculation
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.Flow

class DepositHistoryViewModel(repository: DepositRepository) : ViewModel() {

    val calculations: Flow<List<DepositCalculation>> = repository.calculations
}
