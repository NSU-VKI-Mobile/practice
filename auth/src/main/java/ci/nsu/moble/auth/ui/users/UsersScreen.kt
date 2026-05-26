package ci.nsu.moble.auth.ui.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.auth.ui.users.UsersViewModel
import kotlinx.serialization.InternalSerializationApi

@OptIn(ExperimentalMaterial3Api::class, InternalSerializationApi::class)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    Button(onClick = { viewModel.logout(); onLogout() }) {
                        Text("Выйти")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val s = state) {
                is UsersViewModel.UsersState.FirstLaunch -> CircularProgressIndicator()
                is UsersViewModel.UsersState.Loading -> CircularProgressIndicator()
                is UsersViewModel.UsersState.Success -> {
                    if (s.users.isEmpty()) {
                        Text("Нет пользователей")
                    } else {
                        LazyColumn {
                            items(s.users) { user ->
                                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = user.login, style = MaterialTheme.typography.titleMedium)
                                        Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "Телефон: ${user.phoneNumber}", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "ID: ${user.userId}", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
                is UsersViewModel.UsersState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = s.message, color = MaterialTheme.colorScheme.error)
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