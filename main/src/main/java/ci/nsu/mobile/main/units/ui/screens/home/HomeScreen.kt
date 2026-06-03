package ci.nsu.mobile.main.units.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.units.ui.screens.login.LoginViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    LaunchedEffect(viewModel.logoutTrigger) {
        if(viewModel.logoutTrigger) onLogout()
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Список пользователей", style = MaterialTheme.typography.headlineMedium)
        if (viewModel.isLoading) LinearProgressIndicator()
        LazyColumn {
            items(viewModel.users) {user ->
                Text(text = "${user.login} - ${user.email}")
            }
        }
        Button(onClick = {viewModel.logout()}) { Text("Выйти")}
    }
}