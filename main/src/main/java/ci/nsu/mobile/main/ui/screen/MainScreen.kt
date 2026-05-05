package ci.nsu.mobile.main.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val users by viewModel.users.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Пользователи",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                }
            ) {
                Text("Выйти")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            users.isEmpty() -> {
                Text("Список пользователей пуст")
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = viewModel::loadUsers) {
                    Text("Обновить")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${user.person.lastName} ${user.person.firstName} ${user.person.middleName}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Логин: ${user.login}")
                                Text("Email: ${user.email}")
                                Text("Группа ID: ${user.person.groupId}")
                                Text("Дата рождения: ${user.person.birthDate}")
                            }
                        }
                    }
                }
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            TextButton(
                onClick = {
                    viewModel.clearError()
                    viewModel.loadUsers()
                }
            ) {
                Text("Повторить")
            }
        }
    }
}
