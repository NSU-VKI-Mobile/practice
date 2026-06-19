package ci.nsu.moble.main.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // LiveData для текста
    private val _text = MutableLiveData<String>().apply {
        value = "Hello, World!"
    }
    val text: LiveData<String> = _text

    // LiveData для счетчика
    private val _counter = MutableLiveData(0)
    val counter: LiveData<Int> = _counter

    // LiveData для загрузки
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // StateFlow для UI состояния
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // Метод для кнопки увеличения счетчика
    fun onButtonClick() {
        val currentCount = _counter.value ?: 0
        _counter.value = currentCount + 1
        _uiState.value = _uiState.value.copy(
            message = "Кнопка нажата ${currentCount + 1} раз"
        )
    }

    // Метод для обновления текста
    fun updateText(newText: String) {
        _text.value = newText
        _uiState.value = _uiState.value.copy(
            message = "Текст обновлен: $newText"
        )
    }

    // Метод загрузки данных
    fun loadInitialData() {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Имитация загрузки данных
                delay(2000)
                val data = listOf("Элемент 1", "Элемент 2", "Элемент 3")
                _uiState.value = _uiState.value.copy(
                    data = data,
                    message = "Данные загружены: ${data.size} элементов",
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Неизвестная ошибка",
                    isLoading = false
                )
            } finally {
                _isLoading.value = false
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // Метод для очистки ошибки
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // UI State data class
    data class UiState(
        val message: String = "",
        val error: String? = null,
        val isLoading: Boolean = false,
        val data: List<String> = emptyList()
    )

    override fun onCleared() {
        super.onCleared()
        // Очистка ресурсов
    }
}