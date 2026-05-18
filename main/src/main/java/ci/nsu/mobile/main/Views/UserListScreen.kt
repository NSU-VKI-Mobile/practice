@file:OptIn(ExperimentalMaterial3Api::class)

package ci.nsu.mobile.main.Views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.ViewModels.UserListViewModel

@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
        viewModel.logoutEvent.collect { onLogout() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пользователи") },
                actions = {
                    IconButton(onClick = viewModel::onLogoutClick) {
                        Icon(Icons.Default.Logout, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = modifier.padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Text(errorMessage!!, modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(users) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Логин: ${user.login}")
                                Text("Email: ${user.email}")
                                Text("Телефон: ${user.phoneNumber}")
                                Text("Имя: ${user.person.firstName} ${user.person.lastName}")
                            }
                        }
                    }
                }
            }
        }
    }
}