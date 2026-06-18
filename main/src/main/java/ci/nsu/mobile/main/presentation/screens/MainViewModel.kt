package ci.nsu.mobile.main.presentation.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun navigateTo(screen: String) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun showDetail(id: Long) {
        _uiState.value = _uiState.value.copy(detailId = id)
    }

    fun hideDetail() {
        _uiState.value = _uiState.value.copy(detailId = null)
    }

    fun showResult(data: Map<String, String>) {
        _uiState.value = _uiState.value.copy(resultData = data)
    }

    fun hideResult() {
        _uiState.value = _uiState.value.copy(resultData = null)
    }

    fun resetToMain() {
        _uiState.value = MainUiState()
    }
}

data class MainUiState(
    val selectedTab: Int = 0,
    val currentScreen: String = "main",
    val detailId: Long? = null,
    val resultData: Map<String, String>? = null
)