package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.repository.CatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

data class CatUiState(
    val currentCatUrl: String = "",
    val isLoading: Boolean = false,
    val historyImages: List<CatImage> = emptyList()
)

data class CatImage(
    val id: Long,
    val url: String,
    val date: Date
)

class CatViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CatRepository(application)

    private val _uiState = MutableStateFlow(CatUiState())
    val uiState: StateFlow<CatUiState> = _uiState.asStateFlow()

    init {
        observeHistory()
    }

    fun loadNewCat() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                currentCatUrl = "",
                isLoading = true
            )

            delay(300)

            val width = 400 + Random.nextInt(10)
            val height = 500 + Random.nextInt(10)
            
            val catUrl = "https://placecats.com/$width/$height"

            _uiState.value = _uiState.value.copy(
                currentCatUrl = catUrl,
                isLoading = false
            )

            repository.saveImage(catUrl)
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            repository.getAllImages().collect { images ->
                _uiState.value = _uiState.value.copy(
                    historyImages = images.map { entity ->
                        CatImage(
                            id = entity.id,
                            url = entity.url,
                            date = entity.date
                        )
                    }
                )
            }
        }
    }

    fun deleteImage(image: CatImage) {
        viewModelScope.launch {
            val entity = ci.nsu.mobile.main.data.local.entity.CatImage(
                id = image.id,
                url = image.url,
                date = image.date
            )
            repository.deleteImage(entity)
        }
    }
}
