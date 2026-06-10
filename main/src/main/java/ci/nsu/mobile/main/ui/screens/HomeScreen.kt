package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun HomeScreen(
    vm: AuthViewModel,
    onLogout: () -> Unit
) {

    val currentUser by vm.currentUser.collectAsState()
    val users by vm.users.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Главная",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        currentUser?.let { user ->

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Мой профиль",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text("ID: ${user.userId}")
                    Text("Логин: ${user.login}")
                    Text("Email: ${user.email}")
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        Button(
            onClick = {

                vm.logout()

                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выйти")
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Пользователи",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LazyColumn {

            items(users) { user ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            text = user.login,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(user.email)

                        Text(
                            text = "ID: ${user.userId}"
                        )
                    }
                }
            }
        }
    }

}
