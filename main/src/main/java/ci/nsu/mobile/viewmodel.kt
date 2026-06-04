package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ci.nsu.mobile.main.network.ApiClient
import ci.nsu.mobile.main.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlin.Result
import ci.nsu.mobile.main.model.UserDto
import ci.nsu.mobile.main.data.TokenManager
import ci.nsu.mobile.main.model.GroupDto
import ci.nsu.mobile.main.model.RegisterRequest

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = AuthRepository(ApiClient.create(application))

    val users = MutableLiveData<List<UserDto>>()
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String?>()

    val groups = MutableLiveData<List<GroupDto>>(emptyList())

    fun loadGroups() {
        println("TOKEN = ${TokenManager.getToken(getApplication())}")
        viewModelScope.launch {

            val result = repo.getGroups()

            result.onSuccess {
                println("🔥 GROUPS = $it")
                groups.value = it
            }

            result.onFailure {
                println("❌ GROUP ERROR = ${it.message}")
                error.value = it.message
            }
            result.onSuccess {
                println("GROUPS = $it")
                groups.value = it
            }
        }
    }

    fun login(login: String, password: String, onSuccess: () -> Unit) {

        viewModelScope.launch {

            loading.value = true

            val result = repo.login(login, password)

            loading.value = false

            result.onSuccess { user ->

                user.token?.let {
                    TokenManager.saveToken(getApplication(), it)
                }

                onSuccess()
            }

            result.onFailure {
                error.value = it.message
            }
        }
    }
    fun register(
        request: RegisterRequest,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            loading.value = true

            val result = repo.register(request)

            loading.value = false

            result.onSuccess {
                onSuccess()
            }

            result.onFailure {
                error.value = it.message
            }
        }
    }

    fun loadUsers() {

        viewModelScope.launch {

            loading.value = true

            val result = repo.getUsers()

            loading.value = false

            result.onSuccess {

                users.value = it
            }

            result.onFailure {
                error.value = it.message
            }
        }
    }
}