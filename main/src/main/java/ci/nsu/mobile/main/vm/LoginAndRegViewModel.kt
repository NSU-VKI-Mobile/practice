package ci.nsu.mobile.main.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.api.TokenManager
import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.data.dto.PersonDto
import ci.nsu.mobile.main.api.requestData.RegisterRequest
import ci.nsu.mobile.main.data.dto.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.String


data class LoginAndRegUiState(
    val login: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String? = null,
    val gender: String? = null,
    val group: GroupDto? = null,
    val regLogin: String = "",
    val regPassword: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false
){
    val isBirthDateValid: Boolean get()= birthDate != null && (try{LocalDate.parse(birthDate); true} catch (e: Exception){false})
    val isGenderValid: Boolean get() = gender != null
    val isGroupValid: Boolean get() = group != null
    val isAllCorrect: Boolean get() = isBirthDateValid && isGroupValid && isGenderValid
}

class LoginAndRegViewModel(application: Application, private val authRepository: AuthRepository) : AndroidViewModel(application){
    val allGenders = listOf("Муж","Жен")
    private val _uiState = MutableStateFlow(LoginAndRegUiState())
    val uiState: StateFlow<LoginAndRegUiState> = _uiState.asStateFlow()
    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()
    var errorMessage by mutableStateOf<String?>(null)
    var allGroup by mutableStateOf<List<GroupDto>>(emptyList())
    var allUsers by mutableStateOf<List<UserDto>>(emptyList())
    init{
        TokenManager.init(application.applicationContext)
        loadGroups()
    }
    private fun loadGroups(){
        viewModelScope.launch {
            var isFinish = false
            while (!isFinish) {
                authRepository.getGroups()
                    .onSuccess {
                        allGroup = it
                        isFinish = true
                    }
                    .onFailure { error ->
                        errorMessage = "${error.message}"
                    }
            }
        }
    }


    private suspend fun updateToken(newToken: String?, newLogin: String? = null) {
        _token.value = newToken
        if (newToken != null) {
            TokenManager.token = newToken
            updateUserId(newLogin)
        } else {
            TokenManager.clear()
        }
    }

    private suspend fun updateUserId(newLogin: String? = null){
        authRepository.getUsers()
            .onSuccess { users ->
                TokenManager.userId = users.find { it.login == newLogin }?.id?.toLong() ?: -1
            }
            .onFailure { error ->
                errorMessage = "${error.message}"
            }
    }
    fun loadUsers(){
        viewModelScope.launch {
            authRepository.getUsers()
                .onSuccess {
                    allUsers = it
                }
                .onFailure { error ->
                    errorMessage = "${error.message}"
                }
        }
    }

    fun registry(){
        if (_uiState.value.isLoading) return
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.register(
                RegisterRequest(
                    person = PersonDto(
                        firstName = currentState.firstName,
                        lastName = currentState.lastName,
                        middleName = currentState.middleName,
                        birthDate = currentState.birthDate!!,
                        gender = currentState.gender!!,
                        groupId = currentState.group!!.id
                    ),
                    login = currentState.regLogin,
                    password = currentState.regPassword,
                    email = currentState.email,
                    phoneNumber = currentState.phone,
                    roleId = 1,
                    authAllowed = true
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        login = it.regLogin,
                        password = it.regPassword,
                        firstName = "",
                        lastName = "",
                        middleName = "",
                        birthDate = null,
                        gender = null,
                        group = null,
                        regLogin = "",
                        regPassword = "",
                        email = "",
                        phone = "",
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                errorMessage = error.message
            }
        }
    }

    fun logIn(onSuccess: () -> Unit){
        if (_uiState.value.isLoading) return
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.login(currentState.login, currentState.password)
                .onSuccess { authToken ->
                    updateToken(authToken.token, currentState.login)
                    _uiState.update {
                        it.copy(
                            login = "",
                            password = "",
                            isLoading = false
                        )
                    }
                    onSuccess()
                }.onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    errorMessage = error.message
                }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            updateToken(null)
            _uiState.update { LoginAndRegUiState() }
        }
    }

    fun setLogin(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                login = newValue
            )
        }
    }

    fun setRegLogin(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                regLogin = newValue
            )
        }
    }

    fun setPassword(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                password = newValue
            )
        }
    }

    fun setRegPassword(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                regPassword = newValue
            )
        }
    }

    fun setFirstName(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                firstName = newValue
            )
        }
    }

    fun setLastName(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                lastName = newValue
            )
        }
    }

    fun setMiddleName(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                middleName = newValue
            )
        }
    }

    fun setBirthDate(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                birthDate = newValue
            )
        }
    }

    fun setGroup(newValue: GroupDto){
        _uiState.update { currentState ->
            currentState.copy(
                group = newValue
            )
        }
    }

    fun setGender(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                gender = newValue
            )
        }
    }

    fun setPhone(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                phone = newValue
            )
        }
    }

    fun setEmail(newValue: String){
        _uiState.update { currentState ->
            currentState.copy(
                email = newValue
            )
        }
    }
}