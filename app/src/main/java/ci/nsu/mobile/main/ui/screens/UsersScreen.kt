package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.data.model.UserDto // 🟢 Используем UserDto
import ci.nsu.mobile.main.ui.viewmodel.UsersViewModel

@Composable
fun UsersScreen(
    viewModel: UsersViewModel,
    onUserSelected: (UserDto) -> Unit // 🟢 Тип аргумента UserDto
) {
    LaunchedEffect(Unit) { viewModel.loadUsers() }

    val users by viewModel.users.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    if (error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
        }
    } else if (users.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUserSelected(user) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // 🟢 Используем user.login вместо user.username
                        Text(user.login, style = MaterialTheme.typography.titleMedium)
                        Text(user.email ?: "Email не указан", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}