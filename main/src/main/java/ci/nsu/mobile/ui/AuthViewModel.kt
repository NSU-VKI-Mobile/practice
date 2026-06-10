package ci.nsu.mobile.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.data.AuthRepository
import ci.nsu.mobile.models.*
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Всякие строки ввода, которые мы связываем с текст-филдами на экранах
    var loginText by mutableStateOf("")
    var passwordText by mutableStateOf("")
    var isLoading by mutableStateOf(false) // Крутилка загрузки: true — крутится, false — пропала
    var errorMessage by mutableStateOf<String?>(null) // Сюда падает текст ошибки, если сервак ругнётся
    var isSuccess by mutableStateOf(false) // Флаг, что всё прошло ок, можно переходить на другой экран

    // Куча полей для регистрации, которых изначально не хватало в шаблоне
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var birthDate by mutableStateOf("")
    var gender by mutableStateOf("MALE") // По дефолту ставим парня, на экране можно переключить
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var selectedGroupId by mutableStateOf(0) // Тут храним именно ID выбранной группы (число!)
    var groups by mutableStateOf<List<GroupDto>>(emptyList()) // Сюда скачаем список всех групп для выпадашки

    // Список юзеров для главного экрана (private set, чтоб UI случайно его не стёр)
    var users by mutableStateOf<List<UserDto>>(emptyList())
        private set

    // Кнопка Логина
    fun onLoginClick() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.login(LoginRequest(loginText, passwordText))
                .onSuccess { isSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    // Кнопка Регистрации (самая замороченная)
    fun onRegisterClick() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            // Магия с датой: люди пишут 15.05.2004, а сервак ждёт 2004-05-15. Переворачиваем строку!
            val formattedBirthDate = try {
                val parts = birthDate.split(".")
                if (parts.size == 3) {
                    //parts[2] год, parts[1] месяц, parts[0] день Склеиваем через дефис
                    "${parts[2]}-${parts[1]}-${parts[0]}"
                } else {
                    birthDate // Если ввели какую-то дичь без точек, отправляем как есть, пусть сервак сам разбирается
                }
            } catch (e: Exception) {
                birthDate // Если всё совсем сломалось не падаем, шлём исходный текст
            }

            // Упаковываем данные человека (отчество middleName нам не нужно, ставим null)
            val person = PersonDto(
                firstName = firstName,
                lastName = lastName,
                middleName = null,
                birthDate = formattedBirthDate,
                gender = gender,
                groupId = selectedGroupId // Наш числовой ID улетает в базу
            )

            // Собираем полный запрос на регу строго по ТЗ
            val request = RegisterRequest(
                login = loginText,
                password = passwordText,
                email = email,
                phoneNumber = phone,
                roleId = 1,          // По ТЗ тут всегда должна быть 1, не меняем
                authAllowed = true,  // Без этого флага юзера просто не пустит в систему
                person = person
            )

            repository.register(request)
                .onSuccess { isSuccess = true }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    // Эта штука скачивает группы, запускается сразу при открытии экрана регистрации
    fun loadGroups() {
        android.util.Log.d("API_TEST", "Метод loadGroups во ViewModel запущен")
        viewModelScope.launch {
            repository.getGroups()
                .onSuccess {
                    android.util.Log.d("API_TEST", "Успех! Групп пришло: ${it.size}")
                    groups = it // Запихиваем пришедшие группы в нашу переменную
                }
                .onFailure {
                    android.util.Log.e("API_TEST", "Ошибка в репозитории: ${it.message}")
                    errorMessage = it.message
                }
        }
    }

    // Скачиваем юзеров для MainScreen, когда авторизовались
    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            repository.getUsers()
                .onSuccess { users = it }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }
}
