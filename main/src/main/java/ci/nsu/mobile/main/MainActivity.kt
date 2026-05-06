package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.AuthRepository
import ci.nsu.mobile.main.data.TokenManager
import ci.nsu.mobile.main.data.network.ApiClient
import ci.nsu.mobile.main.data.network.GroupDto
import ci.nsu.mobile.main.data.network.UserDto
import ci.nsu.mobile.main.ui.AuthScreen
import ci.nsu.mobile.main.ui.AuthUiState
import ci.nsu.mobile.main.ui.AuthViewModel
import ci.nsu.mobile.main.ui.RegisterForm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(applicationContext)
        val repository = AuthRepository(ApiClient.create(tokenManager), tokenManager)

        setContent {
            MaterialTheme {
                AuthApp(repository)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
private fun AuthApp(repository: AuthRepository) {
    val viewModel: AuthViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(repository) as T
        }
    })
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PracticeTopBar(
                title = when (state.screen) {
                    AuthScreen.Login -> "Вход"
                    AuthScreen.Register -> "Регистрация"
                    AuthScreen.Users -> "Пользователи"
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            state.message?.let { MessageCard(it) }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            when (state.screen) {
                AuthScreen.Login -> LoginScreen(state, viewModel)
                AuthScreen.Register -> RegisterScreen(state, viewModel)
                AuthScreen.Users -> UsersScreen(state.users, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PracticeTopBar(title: String) {
    TopAppBar(title = { Text(title) })
}

@Composable
private fun MessageCard(message: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LoginScreen(state: AuthUiState, viewModel: AuthViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.login,
            onValueChange = viewModel::onLoginChanged,
            label = { Text("Логин") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text("Пароль") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = viewModel::login,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        TextButton(
            onClick = viewModel::openRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}

@Composable
private fun RegisterScreen(state: AuthUiState, viewModel: AuthViewModel) {
    val form = state.registerForm
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextFieldRow("Фамилия", form.lastName) {
            viewModel.updateRegisterForm { form -> form.copy(lastName = it) }
        }
        TextFieldRow("Имя", form.firstName) {
            viewModel.updateRegisterForm { form -> form.copy(firstName = it) }
        }
        TextFieldRow("Отчество", form.middleName) {
            viewModel.updateRegisterForm { form -> form.copy(middleName = it) }
        }
        TextFieldRow("Дата рождения", form.birthDate) {
            viewModel.updateRegisterForm { form -> form.copy(birthDate = it) }
        }
        TextFieldRow("Пол", form.gender) {
            viewModel.updateRegisterForm { form -> form.copy(gender = it) }
        }
        GroupPicker(form, state.groups, viewModel)
        TextFieldRow("Логин", form.login) {
            viewModel.updateRegisterForm { form -> form.copy(login = it) }
        }
        TextFieldRow("Пароль", form.password, isPassword = true) {
            viewModel.updateRegisterForm { form -> form.copy(password = it) }
        }
        TextFieldRow("Email", form.email, KeyboardType.Email) {
            viewModel.updateRegisterForm { form -> form.copy(email = it) }
        }
        TextFieldRow("Телефон", form.phoneNumber, KeyboardType.Phone) {
            viewModel.updateRegisterForm { form -> form.copy(phoneNumber = it) }
        }
        Button(
            onClick = viewModel::register,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
        TextButton(
            onClick = viewModel::openLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Назад ко входу")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TextFieldRow(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupPicker(
    form: RegisterForm,
    groups: List<GroupDto>,
    viewModel: AuthViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedGroup = groups.firstOrNull { it.id == form.groupId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedGroup?.name ?: "Группа не выбрана",
            onValueChange = {},
            readOnly = true,
            label = { Text("Группа") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.name) },
                    onClick = {
                        viewModel.updateRegisterForm { it.copy(groupId = group.id) }
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun UsersScreen(users: List<UserDto>, viewModel: AuthViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = viewModel::loadUsers, modifier = Modifier.weight(1f)) {
                Text("Обновить")
            }
            Button(onClick = viewModel::logout, modifier = Modifier.weight(1f)) {
                Text("Выйти")
            }
        }
        if (users.isEmpty()) {
            Text("Список пользователей пуст")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(users) { user ->
                    UserCard(user)
                }
            }
        }
    }
}

@Composable
private fun UserCard(user: UserDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(user.displayName, style = MaterialTheme.typography.titleMedium)
            user.login?.let { Text("Логин: $it") }
            user.email?.let { Text("Email: $it") }
            user.phoneNumber?.let { Text("Телефон: $it") }
        }
    }
}
