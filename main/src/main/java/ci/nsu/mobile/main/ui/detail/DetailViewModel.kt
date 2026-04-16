package ci.nsu.mobile.main.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _calculation = MutableLiveData<DepositCalculation?>()
    val calculation: LiveData<DepositCalculation?> = _calculation

    fun loadCalculation(id: Long) {
        viewModelScope.launch {
            val calc = repository.getCalculationById(id)
            _calculation.value = calc
        }
    }
}