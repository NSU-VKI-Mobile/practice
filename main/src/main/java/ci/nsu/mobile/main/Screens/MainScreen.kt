package ci.nsu.mobile.main.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import ci.nsu.mobile.main.ViewModel.AuthViewModel
import ci.nsu.mobile.main.DataModels.UserDto

@Composable
fun MainScreen(viewModel: AuthViewModel,
               onLogout: () -> Unit){
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Users", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onLogout) {
                Text("Log out")
            }
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
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