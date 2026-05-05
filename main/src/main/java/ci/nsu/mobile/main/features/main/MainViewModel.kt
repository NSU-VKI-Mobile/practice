package ci.nsu.mobile.main.features.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.core.base.AppEvent
import ci.nsu.mobile.main.core.base.BaseViewModel
import ci.nsu.mobile.main.core.navigation.Screen
import ci.nsu.mobile.main.data.local.storage.TokenManager
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.UserRepository
import ci.nsu.mobile.main.features.auth.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository,
    private val tokenManager: TokenManager
): BaseViewModel<AppEvent, MainState>(
    initialState = MainState()
) {
    init {
        refresh()
    }
    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getUsers()
                .onSuccess { result ->
                    _state.update { it.copy(users = result) }
                }
                .onFailure { e -> Log.d("1234", e.toString())}
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _event.emit(AppEvent.Navigate(Screen.Login.route))
            tokenManager.clearToken()
        }
    }

    fun exit() {
        viewModelScope.launch {
            _event.emit(AppEvent.Exit())
        }
    }
}