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
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.ui.viewmodels.UsersState
import ci.nsu.mobile.main.ui.viewmodels.UsersViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    viewModel: UsersViewModel = viewModel()
) {
    val usersState by viewModel.usersState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        // Шапка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Пользователи", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = {
                viewModel.logout()
                onLogout()
            }) {
                Text("Выйти")
            }
        }

        HorizontalDivider()

        when (val state = usersState) {
            is UsersState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is UsersState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadUsers() }) {
                            Text("Повторить")
                        }
                    }
                }
            }

            is UsersState.Success -> {
                if (state.users.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Список пользователей пуст")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.users) { user ->
                            UserItem(user)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserItem(user: UserDto) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        val fullName = listOfNotNull(
            user.person?.lastName,
            user.person?.firstName,
            user.person?.middleName
        ).joinToString(" ").ifBlank { user.login }

        Text(fullName, style = MaterialTheme.typography.bodyLarge)
        Text(user.login, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        user.email.let {
            Text(it, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
