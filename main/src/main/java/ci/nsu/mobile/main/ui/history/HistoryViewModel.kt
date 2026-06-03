package ci.nsu.mobile.main.ui.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.mai.database.DepositCalculation

import ci.nsu.mobile.main.repository.DepositRepository
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private lateinit var repository: DepositRepository
    private val _allCalculations = MutableLiveData<List<DepositCalculation>>()
    val allCalculations: LiveData<List<DepositCalculation>> = _allCalculations

    fun init(context: Context) {
        repository = DepositRepository.getInstance(context)
        loadCalculations()
    }

    private fun loadCalculations() {
        viewModelScope.launch {
            repository.getAllCalculations().collect { calculations ->
                _allCalculations.postValue(calculations)
            }
        }
    }
}