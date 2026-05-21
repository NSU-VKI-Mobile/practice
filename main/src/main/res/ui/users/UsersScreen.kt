package ci.nsu.mobile.main.ui.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.viewmodel.UsersState
import ci.nsu.mobile.main.viewmodel.UsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    usersViewModel: UsersViewModel,
    onLogout: () -> Unit
) {
    val usersState by usersViewModel.usersState.collectAsState()

    LaunchedEffect(Unit) {
        usersViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users") },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = usersState) {
                is UsersState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UsersState.Success -> {
                    val users = state.users
                    if (users.isEmpty()) {
                        Text(
                            text = "No users found",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 18.sp
                        )
                    } else {
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
                }
                is UsersState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { usersViewModel.loadUsers() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: UserDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = user.login,
                fontSize = 18.sp,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Email: ${user.email}",
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            user.phoneNumber?.let {
                Text(
                    text = "Phone: $it",
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "ID: ${user.userId}",
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}