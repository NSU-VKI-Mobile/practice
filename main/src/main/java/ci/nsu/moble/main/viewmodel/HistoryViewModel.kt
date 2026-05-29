package ci.nsu.moble.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.api.TokenManager
import ci.nsu.moble.main.data.dao.DepositDao
import ci.nsu.moble.main.data.entities.DepositCalculationEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val dao: DepositDao,
    private val tokenManager: TokenManager
) : ViewModel() {

    // Вся реактивная магия базы данных теперь живет здесь
    @OptIn(ExperimentalCoroutinesApi::class)
    val history = tokenManager.currentUser
        .map { user -> user?.userId }
        .flatMapLatest { userId -> // tracks user (so if user changes in the background, it reacts to it)
            if (userId != null) {
                dao.getCalculationsByUserId(userId)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun delete(record: DepositCalculationEntity) = viewModelScope.launch {
        dao.delete(record)
    }

    fun deleteAll() = viewModelScope.launch {
        val userId = tokenManager.currentUser.value?.userId
        if (userId != null) {
            dao.deleteAllByUserId(userId)
        }
    }
}