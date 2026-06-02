package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.LoginViewModel
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.network.RetrofitInstance
import ci.nsu.mobile.main.utils.TokenManager
import androidx.compose.ui.platform.LocalContext
import ci.nsu.mobile.main.viewmodel.LoginViewModelFactory

@Composable
fun LoginScreen( //Функция, которая создает экран входа
    onLoginSuccess: () -> Unit, //Параметр-функция. Будет вызвана, когда пользователь успешно вошел.
                                // Принимает 0 параметров, ничего не возвращает.
    onNavigateToRegister: () -> Unit //Параметр-функция. Будет вызвана, когда пользователь нажал
                                    // "Нет аккаунта? Зарегистрироваться"
) {
    //Создаем зависимости
    val context = LocalContext.current // Получаем Context Android
    val tokenManager = remember { TokenManager(context) } //Создаем TokenManager и сохраняем его в памяти.
    //remember { ... } - объект создается один раз и не пересоздается при перерисовке экрана (например, при повороте экрана).
    val apiService = remember { RetrofitInstance.create(tokenManager) } //Создаем ApiService (клиент для запросов
                        // к серверу). Передаем туда TokenManager, чтобы он автоматически добавлял токен в запросы.
    val authRepository = remember { AuthRepository(apiService, tokenManager) } //создаем репозиторий, который умеет
                                        // делать запросы через ApiService и сохранять токен через TokenManager.
    //Создаем ViewModel (через фабрику)
    val loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(authRepository)
    )

    //Подписываемся на состояние
    val uiState by loginViewModel.uiState.collectAsState()

    // Очищаем ошибку при изменении полей
    LaunchedEffect(uiState.login, uiState.password) {
        if (uiState.error != null) {
            loginViewModel.clearError()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(), //Растягиваем Box на весь экран
        contentAlignment = Alignment.Center //Все внутренние элементы по умолчанию выравниваются по центру
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Вход в систему",
                style = MaterialTheme.typography.headlineMedium //Стиль текста (крупный заголовок)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField( //поле логина
                value = uiState.login,
                onValueChange = { loginViewModel.onLoginChange(it) },
                label = { Text("Логин") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,//Если идет загрузка (isLoading = true), поле становится неактивным
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) //показываем обычную текстовую клавиатуру
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField( //поле пароля
                value = uiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                visualTransformation = PasswordVisualTransformation(), //Превращает вводимые символы в точки
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password) //Клавиатура для пароля
                                                                                // (может показывать символы)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) { //isLoading = true → показываем кружок загрузки
                CircularProgressIndicator()
            } else { //isLoading = false → показываем кнопку
                Button(
                    onClick = { loginViewModel.login(onLoginSuccess) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Войти")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton( //кнопка-ссылка
                onClick = onNavigateToRegister, //При нажатии вызываем функцию, переданную из NavGraph
                                                // (переход на экран регистрации)
                enabled = !uiState.isLoading //Если идет загрузка - кнопка неактивна
            ) {
                Text("Нет аккаунта? Зарегистрироваться")
            }

            if (uiState.error != null) { //Проверяет, есть ли ошибка в состоянии
                Spacer(modifier = Modifier.height(16.dp)) //Если есть - добавляет отступ
                Text(
                    text = uiState.error ?: "", //Показывает красный текст с ошибкой
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}