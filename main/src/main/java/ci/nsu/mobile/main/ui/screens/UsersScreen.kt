package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import ci.nsu.mobile.main.viewmodel.UsersViewModelFactory
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.network.RetrofitInstance
import ci.nsu.mobile.main.utils.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val apiService = remember { RetrofitInstance.create(tokenManager) }
    val authRepository = remember { AuthRepository(apiService, tokenManager) }
    val usersViewModel: UsersViewModel = viewModel(
        factory = UsersViewModelFactory(authRepository)
    )

    val uiState by usersViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    Button(
                        onClick = { usersViewModel.logout(onLogout) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Выйти")
                    }
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
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error ?: "Ошибка",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { usersViewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
                uiState.users.isEmpty() -> {
                    Text("Нет пользователей")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.users) { user ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    // Добавляем проверку на null для person
                                    if (user.person != null) {
                                        Text(
                                            text = "${user.person.lastName} ${user.person.firstName}",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    } else {
                                        Text(
                                            text = user.login,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    Text(
                                        text = "Логин: ${user.login}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "Email: ${user.email}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}