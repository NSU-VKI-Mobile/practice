package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    onLogout: () -> Unit
) {
    val users by mainViewModel.users.collectAsState()
    val isLoading by mainViewModel.isLoading.collectAsState()
    val error by mainViewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    Button(onClick = onLogout) { Text("Выйти") }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { mainViewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
                users.isEmpty() -> Text("Нет пользователей")
                else -> {
                    LazyColumn {
                        items(users) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Логин: ${user.login}")
                                    Text("Email: ${user.email}")
                                    Text("Телефон: ${user.phoneNumber ?: "не указан"}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}