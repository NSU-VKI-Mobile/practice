package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.ui.viewmodel.MainViewModel
import kotlinx.serialization.InternalSerializationApi

@OptIn(ExperimentalMaterial3Api::class, InternalSerializationApi::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {
    val viewModel: MainViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    TextButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Text("Выйти")
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
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                viewModel.errorMessage != null -> {
                    Text(
                        text = viewModel.errorMessage ?: "",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                viewModel.users.isEmpty() -> {
                    Text(
                        text = "Нет пользователей",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = viewModel.users,
                            key = { user -> user.id }
                        ) { user ->
                            UserCard(user = user)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(InternalSerializationApi::class)
@Composable
fun UserCard(user: UserDto) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Логин: ${user.login}", style = MaterialTheme.typography.titleSmall)
            Text("Email: ${user.email}")
            Text("Телефон: ${user.phoneNumber}")
            user.person?.let { person ->
                Text("Имя: ${person.lastName} ${person.firstName} ${person.middleName ?: ""}")
            }
        }
    }
}