package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.repository.Result
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit
) {
    val usersState by viewModel.usersState.collectAsState()

    // Загружаем пользователей при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Список пользователей", style = MaterialTheme.typography.headlineSmall)

            Button(onClick = {
                // 1. Сначала очищаем данные и токен
                viewModel.logout()
                // 2. Затем выполняем навигацию
                onLogoutClick()
            }) {
                Text("Выйти")
            }
        }

        Spacer(Modifier.height(16.dp))

        when (usersState) {
            is Result.Loading -> CircularProgressIndicator()
            is Result.Success -> {
                val users = (usersState as Result.Success).data
                if (users.isEmpty()) {
                    Text("Список пуст")
                } else {
                    LazyColumn {
                        items(users) { user ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Column(Modifier.padding(16.dp)) {
                                    Text("ID: ${user.userId}")
                                    Text("Login: ${user.login}")
                                    Text("Email: ${user.email}")
                                }
                            }
                        }
                    }
                }
            }
            is Result.Error -> Text("Ошибка: ${(usersState as Result.Error).message}")
            Result.Idle -> TODO()
        }
    }
}