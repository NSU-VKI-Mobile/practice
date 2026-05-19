package ci.nsu.mobile.main.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.Data.Models.GroupDto
import ci.nsu.mobile.main.Data.Models.PersonDto
import ci.nsu.mobile.main.Data.Models.RegisterRequest
import ci.nsu.mobile.main.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    // Поля формы
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _middleName = MutableStateFlow("")
    val middleName: StateFlow<String> = _middleName.asStateFlow()

    private val _birthDate = MutableStateFlow("")
    val birthDate: StateFlow<String> = _birthDate.asStateFlow()

    private val _gender = MutableStateFlow("male")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _groupId = MutableStateFlow(1)
    val groupId: StateFlow<Int> = _groupId.asStateFlow()

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _registerSuccess = MutableSharedFlow<Unit>()
    val registerSuccess: SharedFlow<Unit> = _registerSuccess.asSharedFlow()

    fun updateFirstName(value: String) { _firstName.value = value }
    fun updateLastName(value: String) { _lastName.value = value }
    fun updateMiddleName(value: String) { _middleName.value = value }
    fun updateBirthDate(value: String) { _birthDate.value = value }
    fun updateGender(value: String) { _gender.value = value }
    fun updateGroupId(value: Int) { _groupId.value = value }
    fun updateLogin(value: String) { _login.value = value }
    fun updatePassword(value: String) { _password.value = value }
    fun updateEmail(value: String) { _email.value = value }
    fun updatePhone(value: String) { _phone.value = value }

    fun loadGroups() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getGroups()
            _isLoading.value = false
            if (result.isSuccess) {
                _groups.value = result.getOrNull() ?: emptyList()
            } else {
                val exception = result.exceptionOrNull()
                _errorMessage.value = "Ошибка загрузки групп: ${exception?.message}"
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val person = PersonDto(
                firstName = firstName.value,
                lastName = lastName.value,
                middleName = middleName.value.ifBlank { null },
                birthDate = birthDate.value,
                gender = gender.value,
                groupId = groupId.value
            )
            val request = RegisterRequest(
                login = login.value,
                password = password.value,
                email = email.value,
                phoneNumber = phone.value,
                roleId = 1,
                authAllowed = true,
                person = person
            )
            val result = repository.register(request)
            if (result.isSuccess) {
                _registerSuccess.emit(Unit)
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Ошибка регистрации"
            }
            _isLoading.value = false
        }
    }
}