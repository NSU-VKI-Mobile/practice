package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.TokenManager
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun MainScreen(navController: NavController) {

    val viewModel: AuthViewModel = viewModel()

    val users by viewModel.users.observeAsState(emptyList())
    val error by viewModel.error.observeAsState()

    // авто-загрузка при входе
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp)
    ) {

        Text(
            text = "Пользователи",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(onClick = {
                viewModel.loadUsers()
            }) {
                Text("Обновить")
            }

            Button(onClick = {
                TokenManager.clear(navController.context)

                navController.navigate("login") {
                    popUpTo("main") {
                        inclusive = true
                    }
                }
            }) {
                Text("Выйти")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(users) { user ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = user.login,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text("ID: ${user.id}")

                        Text("Email: ${user.email ?: "-"}")
                    }
                }
            }
        }
    }
}