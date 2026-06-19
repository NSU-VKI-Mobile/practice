package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.viewmodel.UserListViewModel
import ci.nsu.mobile.main.ui.components.LoadingDialog
import ci.nsu.mobile.main.ui.components.UserCard
import ci.nsu.mobile.main.ui.components.ErrorDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    onLogout: () -> Unit,
    viewModel: UserListViewModel = viewModel()
) {
    val state by viewModel.usersState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        ErrorDialog(
            message = state.error!!,
            onDismiss = { /* Ошибка исчезнет при следующей загрузке */ }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.users.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет пользователей", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.users) { user ->
                    UserCard(user = user)
                }
            }
        }
    }
}