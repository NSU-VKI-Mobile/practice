package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.network.dto.GroupDto
import ci.nsu.mobile.main.data.network.dto.PersonDto
import ci.nsu.mobile.main.data.network.dto.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.util.Event
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    data class UiState(
        val firstName: String = "",
        val lastName: String = "",
        val middleName: String = "",
        val birthDate: String = "",
        val gender: String = "",
        val login: String = "",
        val password: String = "",
        val email: String = "",
        val phone: String = "",
        val groups: List<GroupDto> = emptyList(),
        val selectedGroupId: Int? = null,
        val isLoading: Boolean = false,
        val message: String? = null
    )

    private val repository = AuthRepository()
    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _registrationCompleted = MutableLiveData<Event<Unit>>()
    val registrationCompleted: LiveData<Event<Unit>> = _registrationCompleted

    fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            result.fold(
                onSuccess = { groups ->
                    val selected = groups.firstOrNull()?.id
                    _uiState.value = (_uiState.value ?: UiState()).copy(
                        groups = groups,
                        selectedGroupId = selected,
                        message = null
                    )
                },
                onFailure = { throwable ->
                    _uiState.value = (_uiState.value ?: UiState()).copy(
                        message = throwable.message ?: "Не удалось получить группы"
                    )
                }
            )
        }
    }

    fun onGroupSelected(id: Int?) {
        _uiState.value = (_uiState.value ?: UiState()).copy(selectedGroupId = id)
    }

    fun onTextChanged(
        firstName: String,
        lastName: String,
        middleName: String,
        birthDate: String,
        gender: String,
        login: String,
        password: String,
        email: String,
        phone: String
    ) {
        _uiState.value = (_uiState.value ?: UiState()).copy(
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            birthDate = birthDate,
            gender = gender,
            login = login,
            password = password,
            email = email,
            phone = phone,
            message = null
        )
    }

    fun onRegisterClicked() {
        val state = _uiState.value ?: UiState()
        if (
            state.firstName.isBlank() ||
            state.lastName.isBlank() ||
            state.birthDate.isBlank() ||
            state.gender.isBlank() ||
            state.login.isBlank() ||
            state.password.isBlank() ||
            state.email.isBlank() ||
            state.phone.isBlank() ||
            state.selectedGroupId == null
        ) {
            _uiState.value = state.copy(message = "Заполните все обязательные поля")
            return
        }

        val request = RegisterRequest(
            login = state.login.trim(),
            password = state.password,
            email = state.email.trim(),
            phoneNumber = state.phone.trim(),
            person = PersonDto(
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim(),
                middleName = state.middleName.trim().ifBlank { null },
                birthDate = state.birthDate.trim(),
                gender = state.gender.trim(),
                groupId = state.selectedGroupId
            )
        )

        _uiState.value = state.copy(isLoading = true, message = null)
        viewModelScope.launch {
            val result = repository.register(request)
            result.fold(
                onSuccess = {
                    _uiState.value = (_uiState.value ?: UiState()).copy(
                        isLoading = false,
                        message = "Регистрация выполнена"
                    )
                    _registrationCompleted.value = Event(Unit)
                },
                onFailure = { throwable ->
                    _uiState.value = (_uiState.value ?: UiState()).copy(
                        isLoading = false,
                        message = throwable.message ?: "Ошибка регистрации"
                    )
                }
            )
        }
    }
}
