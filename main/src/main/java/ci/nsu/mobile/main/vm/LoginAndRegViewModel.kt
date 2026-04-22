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
import ci.nsu.mobile.main.data.dto.RegisterRequest
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
    val phone: String = ""
){
    val isBirthDateValid: Boolean get()= birthDate != null && (try{LocalDate.parse(birthDate); true} catch (e: Exception){false})
    val isGenderValid: Boolean get() = gender != null
    val isGroupValid: Boolean get() = group != null
    val isAllCorrect: Boolean get() = isBirthDateValid && isGroupValid && isGroupValid
}

class LoginAndRegViewModel(application: Application) : AndroidViewModel(application){
    val repository = AuthRepository()
    val allGenders = listOf<String>("Муж","Жен")
    private val _uiState = MutableStateFlow(LoginAndRegUiState())
    val uiState: StateFlow<LoginAndRegUiState> = _uiState.asStateFlow()
    var errorMessage by mutableStateOf<String?>(null)
    var allGroup by mutableStateOf<List<GroupDto>>(emptyList())
    var allUsers by mutableStateOf<List<UserDto>>(emptyList())
    init{
        // Если ошибка, то что делать?
        viewModelScope.launch {
            var isFinish = false;
            while (!isFinish) {
                repository.getGroups()
                    .onSuccess {
                        allGroup = it
                        isFinish = true
                    }
                    .onFailure { error ->
                        errorMessage = "${error.message}"
                        isFinish = true
                    }
            }
        }
    }
    fun loadUsers(){
        viewModelScope.launch {
            repository.getUsers()
                .onSuccess {
                    allUsers = it
                }
                .onFailure { error ->
                    errorMessage = "${error.message}"
                }
        }
    }

    fun registry(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                var newLogin = currentState.login
                var newPassword = currentState.password
                var newFirstName = currentState.firstName
                var newLastName = currentState.lastName
                var newMiddleName = currentState.middleName
                var newBirthDate= currentState.birthDate
                var newGender = currentState.gender
                var newGroup = currentState.group
                var newRegLogin = currentState.regLogin
                var newRegPassword = currentState.regPassword
                var newEmail = currentState.email
                var newPhone= currentState.phone
                if (currentState.isAllCorrect) {
                    repository.register(
                        RegisterRequest(
                            person = PersonDto(
                                firstName = currentState.firstName,
                                lastName = currentState.lastName,
                                middleName = currentState.middleName,
                                birthDate = currentState.birthDate!!,
                                gender = currentState.gender!!,
                                groupId = currentState.group!!.id),
                            login = currentState.regLogin,
                            password = currentState.regPassword,
                            email = currentState.email,
                            phoneNumber = currentState.phone,
                            roleId = 1,
                            authAllowed = true
                        )
                    )
                        .onSuccess {
                            newLogin = currentState.regLogin
                            newPassword = currentState.regPassword
                            newFirstName = ""
                            newLastName = ""
                            newMiddleName = ""
                            newBirthDate= null
                            newGender = null
                            newGroup = null
                            newRegLogin = ""
                            newRegPassword = ""
                            newEmail = ""
                            newPhone= ""
                        }
                        .onFailure {error ->
                            errorMessage = "${error.message}"
                    }
                }
                currentState.copy(
                    login = newLogin,
                    password = newPassword,
                    firstName = newFirstName,
                    lastName = newLastName,
                    middleName = newMiddleName,
                    birthDate = newBirthDate,
                    gender = newGender,
                    group = newGroup,
                    regLogin = newRegLogin,
                    regPassword = newRegPassword,
                    email = newEmail,
                    phone = newPhone
                )
            }
        }
    }

    fun logIn(){
        viewModelScope.launch {
            _uiState.update { currentState ->
                var newLogin = currentState.login
                var newPassword = currentState.password
                repository.login(currentState.login,currentState.password)
                    .onSuccess {
                        TokenManager.token = it
                        newLogin = ""
                        newPassword = ""
                    }
                    .onFailure { error ->
                        errorMessage = "${error.message}"
                    }
                currentState.copy(
                    login = newLogin,
                    password = newPassword
                )
            }
        }
    }

    fun logOut(){
        TokenManager.clear()
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