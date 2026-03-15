package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _clickCount = MutableStateFlow(0)
    val clickCount = _clickCount.asStateFlow()

    fun onButtonClick() {
        viewModelScope.launch {
            _clickCount.value += 1
        }
    }
}
