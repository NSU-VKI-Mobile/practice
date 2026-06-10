package ci.nsu.mobile.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.auth.TokenManager
import ci.nsu.mobile.auth.data.models.UserDto
import ci.nsu.mobile.auth.viewmodel.UsersState
import ci.nsu.mobile.auth.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    usersViewModel: UsersViewModel,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val state by usersViewModel.usersState.collectAsState()
    var showQrDialog by remember { mutableStateOf(false) }

    // Получаем текущего пользователя
    val currentUserId = TokenManager.userId
    val currentUserLogin = TokenManager.userLogin

    LaunchedEffect(Unit) {
        usersViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    // Кнопка создания QR-кода
                    IconButton(onClick = { showQrDialog = true }) {
                        Icon(Icons.Default.QrCode, contentDescription = "Создать QR-код")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val currentState = state) {
                is UsersState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UsersState.Success -> {
                    UserList(users = currentState.users)
                }
                is UsersState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentState.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { usersViewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }

    // Диалог с QR-кодом
    if (showQrDialog) {
        // Здесь нужно получить логин и пароль текущего пользователя
        // В реальном приложении пароль нужно хранить зашифрованным
        // Для демонстрации используем заглушку
        val userLogin = currentUserLogin ?: "user"
        val userPassword = "user_password" // В реальном приложении нужно получать из безопасного хранилища

        QrCodeDialog(
            login = userLogin,
            password = userPassword,
            onDismiss = { showQrDialog = false }
        )
    }
}

@Composable
fun UserList(users: List<UserDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserItem(user)
        }
    }
}

@Composable
fun UserItem(user: UserDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.login,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "ID: ${user.userId}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Email: ${user.email}",
                style = MaterialTheme.typography.bodyMedium
            )
            user.phoneNumber?.let {
                if (it.isNotBlank()) {
                    Text(
                        text = "Телефон: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}