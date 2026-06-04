package ci.nsu.mobile.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.data.model.*
import ci.nsu.mobile.data.repository.AuthRepository
import ci.nsu.mobile.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    var isLoggedIn by mutableStateOf(TokenManager.token != null)
        private set

    var users by mutableStateOf<List<UserDto>>(emptyList())
    var groups by mutableStateOf<List<GroupDto>>(emptyList())

    private fun logAuthState(tag: String) {
        Log.d("AUTH_STATE", "====================")
        Log.d("AUTH_STATE", tag)
        Log.d("AUTH_STATE", "token = ${TokenManager.token}")
        Log.d("AUTH_STATE", "userLogin = ${TokenManager.userLogin}")
        Log.d("AUTH_STATE", "isLoggedIn = $isLoggedIn")
        Log.d("AUTH_STATE", "====================")
    }

    private fun refreshAuthState() {
        isLoggedIn = TokenManager.token != null
        logAuthState("refreshAuthState()")
    }

    init {
        syncAuthState()
    }

    private fun syncAuthState() {
        isLoggedIn = TokenManager.token != null
        logAuthState("syncAuthState() INIT")
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {

            Log.d("AUTH", "LOGIN ATTEMPT login=$login password=***")

            isLoading = true
            error = null

            val result = repo.login(login, password)

            isLoading = false

            result.onSuccess {

                TokenManager.token = it.token
                TokenManager.userLogin = login

                Log.d("AUTH", "LOGIN SUCCESS")
                Log.d("AUTH", "login=$login")
                Log.d("AUTH", "server userId=${it.userId}")
                Log.d("AUTH", "token=${it.token}")

                refreshAuthState()

                logAuthState("after LOGIN SUCCESS")

            }.onFailure {

                Log.e("AUTH", "LOGIN FAILED: ${it.message}")

                error = it.message
                TokenManager.clear()

                refreshAuthState()
                logAuthState("after LOGIN FAILURE")
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {

            Log.d("AUTH", "REGISTER ATTEMPT login=${request.login}")

            isLoading = true
            error = null

            val result = repo.register(request)

            isLoading = false

            result.onSuccess {

                TokenManager.token = it.token
                // ИСПРАВЛЕНО: берем логин из объекта request
                TokenManager.userLogin = request.login

                Log.d("AUTH", "REGISTER SUCCESS")
                Log.d("AUTH", "login=${request.login}")
                Log.d("AUTH", "server userId=${it.userId}")
                Log.d("AUTH", "token=${it.token}")

                refreshAuthState()
                logAuthState("after REGISTER SUCCESS")

            }.onFailure {

                Log.e("AUTH", "REGISTER FAILED: ${it.message}")

                error = it.message
                TokenManager.clear()

                refreshAuthState()
                logAuthState("after REGISTER FAILURE")
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {

            Log.d("API", "Loading users...")
            logAuthState("before loadUsers")

            repo.getUsers()
                .onSuccess {
                    users = it
                    Log.d("API", "Users size = ${it.size}")
                }
                .onFailure {
                    Log.e("API", "ERROR: ${it.message}")
                }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {

            Log.d("API", "Loading groups...")
            logAuthState("before loadGroups")

            repo.getGroups()
                .onSuccess {
                    groups = it
                }
                .onFailure {
                    Log.e("API", "GROUP ERROR: ${it.message}")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {

            Log.d("AUTH", "LOGOUT CLICKED")

            TokenManager.clear()
            repo.resetApi()

            users = emptyList()
            groups = emptyList()

            error = null
            isLoading = false
            isLoggedIn = false

            logAuthState("after LOGOUT")

            Log.d("AUTH", "LOGOUT SUCCESS")
        }
    }
}