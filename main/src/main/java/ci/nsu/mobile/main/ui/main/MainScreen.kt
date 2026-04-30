package ci.nsu.mobile.main.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.viewmodel.UiState
import ci.nsu.mobile.main.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: UserViewModel,
    onLogout: () -> Unit
) {
    val usersState by viewModel.usersState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Обработка ошибок
    LaunchedEffect(usersState) {
        if (usersState is UiState.Error) {
            snackbarHostState.showSnackbar((usersState as UiState.Error).message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Filled.ExitToApp,
                            contentDescription = "Выйти"
                        )
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
            when (val state = usersState) {
                is UiState.Idle -> {
                    Text(
                        text = "Загрузка данных...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val users = state.data
                    if (users.isEmpty()) {
                        Text(
                            text = "Список пользователей пуст",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        UserList(users = users)
                    }
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ошибка: ${state.message}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserList(users: List<UserDto>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            UserCard(user = user)
        }
    }
}

@Composable
private fun UserCard(user: UserDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val person = user.person
            val fullName = if (person != null) {
                "${person.lastName} ${person.firstName}${person.middleName?.let { " $it" } ?: ""}"
            } else {
                user.login
            }

            Text(
                text = fullName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "📧 ${user.email ?: "Не указан"}", style = MaterialTheme.typography.bodyMedium)
            user.phoneNumber?.let {
                Text(text = "📱 $it", style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "👤 Логин: ${user.login}", style = MaterialTheme.typography.bodySmall)

            person?.let {
                Text(text = " Группа: ${it.groupId}", style = MaterialTheme.typography.bodySmall)
                Text(text = "🎂 ${it.birthDate}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}