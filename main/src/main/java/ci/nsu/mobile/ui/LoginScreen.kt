package ci.nsu.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions // Добавили для управления кнопками клавиатуры
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction // Кнопки "Далее" и "Готово"
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(viewModel: AuthViewModel, onNavigateToRegister: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Вход в систему", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода ЛОГИНА
        OutlinedTextField(
            value = viewModel.loginText,
            onValueChange = { viewModel.loginText = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next // Меняет кнопку Enter на клавиатуре на стрелочку "Далее"
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ввода ПАРОЛЯ
        OutlinedTextField(
            value = viewModel.passwordText,
            onValueChange = { viewModel.passwordText = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(), // Скрывает пароль звездочками
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done // Меняет кнопку Enter на галочку "Готово" (прячет клаву)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Если идёт запрос к серверу показываем крутилку, иначе — кнопку входа
        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Войти")
            }
        }

        // Если бэкенд вернул ошибку (например, "Неверный пароль"), рисуем красный текст
        viewModel.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Кнопочка переключения на экран регистрации
        TextButton(onClick = onNavigateToRegister) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}
