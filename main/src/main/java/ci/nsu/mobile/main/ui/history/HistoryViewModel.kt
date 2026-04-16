package ci.nsu.mobile.main.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _calculations = MutableLiveData<List<DepositCalculation>>()
    val calculations: LiveData<List<DepositCalculation>> = _calculations

    init {
        viewModelScope.launch {
            repository.getAllCalculations().collect { list ->
                _calculations.value = list
            }
        }
    }
}