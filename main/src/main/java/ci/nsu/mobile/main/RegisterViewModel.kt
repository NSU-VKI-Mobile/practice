package ci.nsu.mobile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ci.nsu.mobile.main.data.models.GroupDto

class RegisterViewModel(
    private val authRepository: AuthRepository
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

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender.asStateFlow()

    private val _groupId = MutableStateFlow<Int?>(null)
    val groupId: StateFlow<Int?> = _groupId.asStateFlow()

    private val _login = MutableStateFlow("")
    val login: StateFlow<String> = _login.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    // Состояния UI
    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            _isLoading.update { true }
            val result = authRepository.getGroups()
            result.onSuccess { groups ->
                _groups.update { groups }
            }.onFailure { exception ->
                _error.update { "Ошибка загрузки групп: ${exception.message}" }
            }
            _isLoading.update { false }
        }
    }

    fun updateFirstName(value: String) { _firstName.update { value } }
    fun updateLastName(value: String) { _lastName.update { value } }
    fun updateMiddleName(value: String) { _middleName.update { value } }
    fun updateBirthDate(value: String) { _birthDate.update { value } }
    fun updateGender(value: String) { _gender.update { value } }
    fun updateGroupId(value: Int?) { _groupId.update { value } }
    fun updateLogin(value: String) { _login.update { value } }
    fun updatePassword(value: String) { _password.update { value } }
    fun updateEmail(value: String) { _email.update { value } }
    fun updatePhoneNumber(value: String) { _phoneNumber.update { value } }

    fun clearError() { _error.update { null } }

    fun onRegisterClick(onSuccess: () -> Unit) {
        // Валидация
        if (_firstName.value.isBlank() || _lastName.value.isBlank() ||
            _login.value.isBlank() || _password.value.isBlank() ||
            _email.value.isBlank() || _groupId.value == null) {
            _error.update { "Заполните все обязательные поля" }
            return
        }

        viewModelScope.launch {
            _isLoading.update { true }
            _error.update { null }

            val person = PersonDto(
                firstName = _firstName.value,
                lastName = _lastName.value,
                middleName = _middleName.value,
                birthDate = _birthDate.value,
                gender = _gender.value,
                groupId = _groupId.value!!
            )

            val request = RegisterRequest(
                login = _login.value,
                password = _password.value,
                email = _email.value,
                phoneNumber = _phoneNumber.value,
                person = person
            )

            val result = authRepository.register(request)

            _isLoading.update { false }

            result.onSuccess {
                onSuccess()
            }.onFailure { exception ->
                _error.update { exception.message ?: "Ошибка регистрации" }
            }
        }
    }
}