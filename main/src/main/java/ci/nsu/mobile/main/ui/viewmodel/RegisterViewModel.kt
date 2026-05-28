package ci.nsu.mobile.main.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val availableGroups: List<GroupDto> = emptyList(),
    val fieldErrors: FieldErrors = FieldErrors()
)

data class FieldErrors(
    val firstName: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val birthDate: String? = null,
    val login: String? = null,
    val password: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val group: String? = null
)

class RegisterViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun loadGroups() {
        viewModelScope.launch {
            val result = repository.getGroups()
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    availableGroups = result.getOrNull() ?: emptyList()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateAndRegister(
        firstName: String,
        lastName: String,
        middleName: String,
        birthDate: String,
        gender: String,
        groupId: Int,
        login: String,
        password: String,
        email: String,
        phoneNumber: String
    ) {
        val fieldErrors = FieldErrors(
            firstName = validateName(firstName, "Имя"),
            lastName = validateName(lastName, "Фамилия"),
            middleName = if (middleName.isNotBlank()) validateName(middleName, "Отчество") else null,
            birthDate = validateBirthDate(birthDate),
            login = validateLogin(login),
            password = validatePassword(password),
            email = validateEmail(email),
            phone = validatePhone(phoneNumber),
            group = if (groupId == 0) "Выберите группу" else null
        )

        val hasErrors = fieldErrors.let { errors ->
            listOf(
                errors.firstName, errors.lastName, errors.middleName,
                errors.birthDate, errors.login, errors.password,
                errors.email, errors.phone, errors.group
            ).any { it != null }
        }

        _uiState.value = _uiState.value.copy(fieldErrors = fieldErrors)

        if (!hasErrors) {
            register(
                login = login,
                password = password,
                email = email,
                phoneNumber = formatPhoneNumber(phoneNumber),
                firstName = firstName.trim(),
                lastName = lastName.trim(),
                middleName = middleName.trim().ifEmpty { null },
                birthDate = birthDate,
                gender = gender,
                groupId = groupId
            )
        }
    }

    private fun register(
        login: String,
        password: String,
        email: String,
        phoneNumber: String,
        firstName: String,
        lastName: String,
        middleName: String?,
        birthDate: String,
        gender: String,
        groupId: Int
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, fieldErrors = FieldErrors())

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
                phoneNumber = phoneNumber,
                person = person
            )

            val result = repository.register(request)

            _uiState.value = if (result.isSuccess) {
                RegisterUiState(isSuccess = true)
            } else {
                RegisterUiState(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Registration failed"
                )
            }
        }
    }

    fun resetSuccessState() {
        _uiState.value = RegisterUiState()
    }

    // Валидация имени
    private fun validateName(value: String, fieldName: String): String? {
        val nameRegex = Regex("^[A-Za-zА-Яа-я\\s-]{2,50}$")
        return when {
            value.isBlank() -> "$fieldName обязательно"
            value.any { it.isDigit() } -> "Цифры запрещены"
            value.length < 2 -> "Минимум 2 символа"
            value.length > 50 -> "Максимум 50 символов"
            !nameRegex.matches(value.trim()) -> "Только буквы, пробелы и дефисы"
            else -> null
        }
    }

    // Валидация даты рождения
    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateBirthDate(value: String): String? {
        val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        if (value.isBlank()) return "Дата рождения обязательна"
        if (!dateRegex.matches(value)) return "Формат ГГГГ-ММ-ДД"

        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(value, formatter)
            val today = LocalDate.now()

            when {
                date.isAfter(today) -> "Дата не может быть в будущем"
                date.year < 1900 -> "Год должен быть не ранее 1900"
                else -> null
            }
        } catch (e: DateTimeParseException) {
            "Неверный формат даты"
        }
    }

    // Валидация логина
    private fun validateLogin(value: String): String? {
        val loginRegex = Regex("^[A-Za-z0-9]+$")
        return when {
            value.isBlank() -> "Логин обязателен"
            value.length < 3 -> "Минимум 3 символа"
            value.length > 30 -> "Максимум 30 символов"
            value.contains(" ") -> "Пробелы запрещены"
            value.any { it.isLetter() && !it.isASCII() } -> "Только английские буквы"
            !loginRegex.matches(value) -> "Только латиница и цифры"
            else -> null
        }
    }

    // Валидация пароля
    private fun validatePassword(value: String): String? {
        return when {
            value.isBlank() -> "Пароль обязателен"
            value.length < 4 -> "Минимум 4 символа"
            value.length > 50 -> "Максимум 50 символов"
            value.contains(" ") -> "Пробелы запрещены"
            else -> null
        }
    }

    // Валидация email
    private fun validateEmail(value: String): String? {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return when {
            value.isBlank() -> "Email обязателен"
            value.contains(" ") -> "Пробелы запрещены"
            value.any { it.isLetter() && !it.isASCII() } -> "Только английские буквы"
            !emailRegex.matches(value) -> "Неверный формат email"
            else -> null
        }
    }

    // Валидация телефона
    private fun validatePhone(value: String): String? {
        return when {
            value.isBlank() -> "Телефон обязателен"
            value.length != 11 -> "Должно быть 11 цифр"
            !value.startsWith("8") -> "Должен начинаться с 8"
            !value.all { it.isDigit() } -> "Только цифры"
            else -> null
        }
    }

    // Форматирование телефона для отправки
    private fun formatPhoneNumber(phone: String): String {
        // Преобразуем 8XXXXXXXXXX в +7XXXXXXXXXX
        return if (phone.startsWith("8") && phone.length == 11) {
            "+7${phone.drop(1)}"
        } else {
            phone
        }
    }

    fun clearFieldError(field: String) {
        val currentErrors = _uiState.value.fieldErrors
        val newErrors = when (field) {
            "firstName" -> currentErrors.copy(firstName = null)
            "lastName" -> currentErrors.copy(lastName = null)
            "middleName" -> currentErrors.copy(middleName = null)
            "birthDate" -> currentErrors.copy(birthDate = null)
            "login" -> currentErrors.copy(login = null)
            "password" -> currentErrors.copy(password = null)
            "email" -> currentErrors.copy(email = null)
            "phone" -> currentErrors.copy(phone = null)
            else -> currentErrors
        }
        _uiState.value = _uiState.value.copy(fieldErrors = newErrors)
    }
}

fun Char.isASCII(): Boolean = this.code in 0..127