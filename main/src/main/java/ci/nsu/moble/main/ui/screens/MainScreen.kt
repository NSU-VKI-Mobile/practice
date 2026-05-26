package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.moble.main.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    viewModel: AuthViewModel
) {
    val users by viewModel.users.collectAsState()
    val usersLoading by viewModel.usersLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // при первом открытии грузим список
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Пользователи") },
            actions = {
                Button(onClick = { viewModel.logout(); onLogout() }) {
                    Text("Выйти")
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                usersLoading -> CircularProgressIndicator()
                error != null -> {
                    Column {
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
                users.isEmpty() -> Text("Нет пользователей", fontSize = 18.sp)
                else -> LazyColumn {
                    items(users) { user ->
                        Text("${user.login} - ${user.email}")
                    }
                }
            }
        }
    }
}