package ci.nsu.mobile.main.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import ci.nsu.mobile.main.MainActivity
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.model.UserDto

@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Пользователи",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(onClick = onLogout) {
                Text("Выйти")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }

        viewModel.error?.let {
            Text(it, color = Color.Red)
        }

        LazyColumn {
            items(viewModel.users) { user ->
                UserItem(user)
            }
        }
    }
}


@Composable
fun UserItem(user: UserDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("ID: ${user.id}")
            Text("Login: ${user.login}")
            Text("Email: ${user.email}")
        }
    }
}