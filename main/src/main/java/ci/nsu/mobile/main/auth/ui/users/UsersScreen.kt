package ci.nsu.mobile.main.auth.ui.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.auth.data.dto.UserDto
import ci.nsu.mobile.main.auth.ui.components.ProgressBar
import ci.nsu.mobile.main.auth.ui.components.UserCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    onLogout: () -> Unit
) {
    val viewModel: UsersViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    //val currentUserId by viewModel.currentUserId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    Button(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        },
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
                .padding(paddingValues)
        ) {
            when (val state = uiState.usersState) {
                is UsersState.Loading -> {
                    ProgressBar()
                }
                is UsersState.Success -> {
                    UserList(
                        users = state.users,
                        currentUserId = uiState.currentUserId //currentUserId
                    )
                }
                is UsersState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.loadUsers() }
                    )
                }
                is UsersState.Idle -> { }
            }
        }
    }
}

@Composable
fun UserList(
    users: List<UserDto>,
    currentUserId: Long?
) {
    if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Пользователи не найдены")
        }
        return
    }

    // Split users into current user and others
    val currentUser = if (currentUserId != null) {
        users.find { it.id == currentUserId }
    } else null

    val otherUsers = if (currentUserId != null) {
        users.filter { it.id != currentUserId }
    } else {
        users
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Pinned current user at top
        if (currentUser != null) {
            item {
                Text(
                    text = "Вы",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UserCard(
                    user = currentUser,
                    isCurrentUser = true
                )
            }

            if (otherUsers.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Другие пользователи",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }

        // Other users
        items(otherUsers) { user ->
            UserCard(
                user = user,
                isCurrentUser = false
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ошибка",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Повторить")
        }
    }
}