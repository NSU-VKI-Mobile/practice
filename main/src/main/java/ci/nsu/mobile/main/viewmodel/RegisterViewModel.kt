package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.GroupDto
import ci.nsu.mobile.main.data.models.PersonDto
import ci.nsu.mobile.main.data.models.RegisterRequest
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    // ============== СОСТОЯНИЕ ФОРМЫ ==============
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // ============== СПИСОК ГРУПП ==============
    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups

    // ============== ЗАГРУЗКА ГРУПП ПРИ СОЗДАНИИ ==============
    init {
        loadGroups()
    }

    // ============== ОБНОВЛЕНИЕ ПОЛЕЙ ФОРМЫ ==============
    fun updateField(field: String, value: String) {
        when (field) {
            "firstName" -> _uiState.value = _uiState.value.copy(firstName = value)
            "lastName" -> _uiState.value = _uiState.value.copy(lastName = value)
            "middleName" -> _uiState.value = _uiState.value.copy(middleName = value)
            "birthDate" -> _uiState.value = _uiState.value.copy(birthDate = value)
            "gender" -> _uiState.value = _uiState.value.copy(gender = value)
            "login" -> _uiState.value = _uiState.value.copy(login = value)
            "password" -> _uiState.value = _uiState.value.copy(password = value)
            "email" -> _uiState.value = _uiState.value.copy(email = value)
            "phoneNumber" -> _uiState.value = _uiState.value.copy(phoneNumber = value)
        }
    }

    // ============== ВЫБОР ГРУППЫ ==============
    fun selectGroup(groupId: Int) {
        _uiState.value = _uiState.value.copy(selectedGroupId = groupId)
    }

    // ============== ОЧИСТКА ОШИБКИ ==============
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // ============== ВАЛИДАЦИЯ ДАННЫХ (ПРОВЕРКА ПЕРЕД ОТПРАВКОЙ) ==============
    /**
     * Проверяет данные перед отправкой на сервер
     * @return Pair(успех, сообщение об ошибке)
     *         если успех = true, то данные валидны
     *         если успех = false, то сообщение содержит текст ошибки
     */
    private fun validateData(): Pair<Boolean, String> {

        // 1. ПРОВЕРКА ФАМИЛИИ (обязательное поле)
        if (_uiState.value.lastName.isBlank()) {
            return Pair(false, "❌ Поле 'Фамилия' обязательно для заполнения")
        }

        // 2. ПРОВЕРКА ИМЕНИ (обязательное поле)
        if (_uiState.value.firstName.isBlank()) {
            return Pair(false, "❌ Поле 'Имя' обязательно для заполнения")
        }

        // 3. ПРОВЕРКА ЛОГИНА (обязательное поле)
        if (_uiState.value.login.isBlank()) {
            return Pair(false, "❌ Поле 'Логин' обязательно для заполнения")
        }

        // 4. ПРОВЕРКА ПАРОЛЯ (минимум 4 символа)
        if (_uiState.value.password.length < 4) {
            return Pair(false, "❌ Пароль должен содержать минимум 4 символа")
        }

        // 5. ПРОВЕРКА ВЫБОРА ГРУППЫ
        if (_uiState.value.selectedGroupId == 0) {
            return Pair(false, "❌ Пожалуйста, выберите группу из списка")
        }

        // 6. ПРОВЕРКА ДАТЫ РОЖДЕНИЯ (формат ГГГГ-ММ-ДД)
        val birthDate = _uiState.value.birthDate
        val datePattern = Regex("""^\d{4}-\d{2}-\d{2}$""")

        if (birthDate.isBlank()) {
            return Pair(false, "❌ Поле 'Дата рождения' обязательно для заполнения\n   Формат: ГГГГ-ММ-ДД (например, 2007-10-10)")
        }

        if (!datePattern.matches(birthDate)) {
            return Pair(false, "❌ Неверный формат даты рождения\n   Нужно: ГГГГ-ММ-ДД (например, 2007-10-10)\n   Вы ввели: $birthDate")
        }

        // Проверка корректности даты
        try {
            val parts = birthDate.split("-")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            if (year < 1900 || year > 2026) {
                return Pair(false, "❌ Год рождения должен быть между 1900 и 2026")
            }
            if (month < 1 || month > 12) {
                return Pair(false, "❌ Месяц должен быть от 01 до 12")
            }
            if (day < 1 || day > 31) {
                return Pair(false, "❌ День должен быть от 01 до 31")
            }

            // Дополнительная проверка на правильность даты (например, 31 февраля не пройдет)
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, 1)
            val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            if (day > maxDay) {
                return Pair(false, "❌ Неверная дата: в $month месяце не может быть $day дней")
            }

        } catch (e: Exception) {
            return Pair(false, "❌ Неверный формат даты\n   Используйте: ГГГГ-ММ-ДД (например, 2007-10-10)")
        }

        // 7. ПРОВЕРКА ПОЛА (выбирается из списка, но на всякий случай остается)
        val gender = _uiState.value.gender
        if (gender.isBlank()) {
            return Pair(false, "❌ Пожалуйста, выберите пол из списка")
        }
        if (gender != "MALE" && gender != "FEMALE") {
            return Pair(false, "❌ Неверный формат пола\n   Используйте MALE (мужской) или FEMALE (женский)\n   Вы ввели: $gender")
        }

        // 8. ПРОВЕРКА EMAIL (если поле не пустое)
        val email = _uiState.value.email
        if (email.isNotBlank()) {
            val emailPattern = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
            if (!emailPattern.matches(email)) {
                return Pair(false, "❌ Неверный формат email\n   Пример: name@example.com\n   Вы ввели: $email")
            }
        }

        // 9. ПРОВЕРКА ТЕЛЕФОНА (если поле не пустое)
        val phone = _uiState.value.phoneNumber
        if (phone.isNotBlank()) {
            val phonePattern = Regex("""^\+?[0-9\s\-()]{10,20}$""")
            if (!phonePattern.matches(phone)) {
                return Pair(false, "❌ Неверный формат телефона\n   Пример: +7 912 345-67-89\n   Вы ввели: $phone")
            }
        }

        // Все проверки пройдены - данные валидны
        return Pair(true, "")
    }

    // ============== РЕГИСТРАЦИЯ ==============
    fun register(onSuccess: () -> Unit) {
        // 1. СНАЧАЛА ПРОВЕРЯЕМ ДАННЫЕ НА СТОРОНЕ КЛИЕНТА
        val (isValid, errorMessage) = validateData()

        // Если данные невалидны - показываем ошибку и НЕ отправляем запрос на сервер
        if (!isValid) {
            _uiState.value = _uiState.value.copy(error = errorMessage)
            return
        }

        // 2. ДАННЫЕ ВАЛИДНЫ - ОТПРАВЛЯЕМ ЗАПРОС НА СЕРВЕР
        viewModelScope.launch {
            // Показываем прогресс и убираем старую ошибку
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Приводим пол к правильному формату (заглавные буквы)
            val correctedGender = _uiState.value.gender.uppercase()

            // Собираем данные о человеке
            val person = PersonDto(
                firstName = _uiState.value.firstName,
                lastName = _uiState.value.lastName,
                middleName = _uiState.value.middleName,
                birthDate = _uiState.value.birthDate,
                gender = correctedGender,
                groupId = _uiState.value.selectedGroupId
            )

            // Собираем полный запрос на регистрацию
            val request = RegisterRequest(
                login = _uiState.value.login,
                password = _uiState.value.password,
                email = _uiState.value.email,
                phoneNumber = _uiState.value.phoneNumber,
                roleId = 1,
                authAllowed = true,
                person = person
            )

            // Отправляем запрос на сервер
            val result = authRepository.register(request)

            // Обрабатываем результат
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()  // Переход на экран входа
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Неизвестная ошибка"
                )
            }
        }
    }

    // ============== ЗАГРУЗКА СПИСКА ГРУПП ==============
    private fun loadGroups() {
        viewModelScope.launch {
            val result = authRepository.getGroups()
            result.onSuccess { groups ->
                _groups.value = groups
            }.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    error = exception.message ?: "Не удалось загрузить группы"
                )
            }
        }
    }
}

// ============== СОСТОЯНИЕ ЭКРАНА РЕГИСТРАЦИИ ==============
data class RegisterUiState(
    val firstName: String = "",      // Имя
    val lastName: String = "",       // Фамилия
    val middleName: String = "",     // Отчество
    val birthDate: String = "",      // Дата рождения
    val gender: String = "",         // Пол (MALE/FEMALE)
    val login: String = "",          // Логин
    val password: String = "",       // Пароль
    val email: String = "",          // Email
    val phoneNumber: String = "",    // Телефон
    val selectedGroupId: Int = 0,    // ID выбранной группы (0 = не выбрана)
    val isLoading: Boolean = false,  // Показывать прогресс?
    val error: String? = null        // Текст ошибки
)