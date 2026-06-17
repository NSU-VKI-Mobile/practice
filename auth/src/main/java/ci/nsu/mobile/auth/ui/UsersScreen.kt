package ci.nsu.mobile.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.domain.model.UserDto

@Composable
fun UsersScreen(
    viewModel: AuthViewModel,
    onUserSelected: (UserDto) -> Unit
) {
    // Загружаем пользователей при открытии
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    val users by viewModel.users.collectAsState()

    if (users.isEmpty()) {
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
                        Text(user.login, style = MaterialTheme.typography.titleMedium)
                        Text(user.email ?: "Email не указан", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}