package ci.nsu.mobile.main.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.DepositApplication
import ci.nsu.mobile.main.model.DepositCalculation
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val repository = DepositApplication.getInstance().repository

    private val _calculations = MutableLiveData<List<DepositCalculation>>()
    val calculations: LiveData<List<DepositCalculation>> = _calculations

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _clearSuccess = MutableLiveData<Boolean>()
    val clearSuccess: LiveData<Boolean> = _clearSuccess

    init {
        loadCalculations()
    }

    fun loadCalculations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllCalculations().collect { calculations ->
                    _calculations.value = calculations
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка загрузки истории: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteAllCalculations()
                _clearSuccess.value = true
                loadCalculations()
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка очистки истории: ${e.message}"
                _isLoading.value = false
            }
        }
    }
}