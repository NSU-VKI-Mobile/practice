package ci.nsu.mobile.main.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.dto.GroupDto
import ci.nsu.mobile.main.data.dto.PersonDto
import ci.nsu.mobile.main.data.dto.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    var groups by mutableStateOf<List<GroupDto>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            loading = true
            error = null
            val result = repository.getGroups()
            loading = false
            result.onSuccess { groups = it }
            result.onFailure { error = it.message ?: "Ошибка загрузки групп" }
        }
    }

    fun register(
        firstName: String,
        lastName: String,
        middleName: String?,
        birthDate: String,
        gender: String,
        groupId: Int,
        login: String,
        password: String,
        email: String,
        phone: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            loading = true
            error = null

            try {
                val person = PersonDto(
                    firstName = firstName,
                    lastName = lastName,
                    middleName = middleName,
                    birthDate = birthDate,
                    gender = gender,
                    groupId = groupId
                )

                val request = RegisterRequest(
                    login = login,
                    password = password,
                    email = email,
                    phoneNumber = phone,
                    roleId = 1,          // по умолчанию – обычный пользователь
                    authAllowed = true,
                    person = person
                )

                val result = repository.register(request)

                result.onSuccess {
                    Log.d("REGISTER", "Регистрация успешна")
                    onSuccess()
                }

                result.onFailure { exception ->
                    Log.e("REGISTER", "Ошибка регистрации", exception)
                    error = exception.message ?: "Ошибка регистрации"
                }
            } catch (e: Exception) {
                Log.e("REGISTER", "Исключение", e)
                error = e.message ?: "Неизвестная ошибка"
            }

            loading = false
        }
    }
}