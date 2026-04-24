package ci.nsu.moble.main.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.app.DepositApp
import ci.nsu.moble.main.data.database.DepositCalculationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as DepositApp).repository
    private val _calculation = MutableStateFlow<DepositCalculationEntity?>(null)
    val calculation: StateFlow<DepositCalculationEntity?> = _calculation.asStateFlow()

    fun loadCalculation(id: Long) {
        if (id <= 0) return
        viewModelScope.launch {
            _calculation.value = repository.getCalculationById(id)
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(application) as T
            }
        }
    }
}