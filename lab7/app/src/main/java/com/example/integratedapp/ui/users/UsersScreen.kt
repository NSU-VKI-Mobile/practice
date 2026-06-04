package com.example.integratedapp.ui.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integratedapp.data.model.UserDto
import com.example.integratedapp.di.ServiceLocator

@Composable
fun UsersScreen(
    viewModel: UsersViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(error, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer)
                    TextButton(onClick = { viewModel.loadUsers() }) { Text("Повторить") }
                }
            }
        }

        if (uiState.users.isEmpty() && !uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Список пуст")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.users) { user ->
                    UserCard(user = user, onClick = { viewModel.selectUser(user) })
                }
            }
        }
    }

    // Диалог с деталями пользователя
    uiState.selectedUser?.let { user ->
        AlertDialog(
            onDismissRequest = { viewModel.selectUser(null) },
            title = { Text("Информация о пользователе") },
            text = {
                Column {
                    val name = listOfNotNull(user.person?.lastName, user.person?.firstName, user.person?.middleName)
                        .joinToString(" ")
                    if (name.isNotBlank()) Text("ФИО: $name")
                    user.login?.let { Text("Логин: $it") }
                    user.email?.let { Text("Email: $it") }
                    user.phoneNumber?.let { Text("Телефон: $it") }
                    user.person?.birthDate?.let { Text("Дата рождения: $it") }
                    user.person?.gender?.let { Text("Пол: ${if (it == "M") "Мужской" else "Женский"}") }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.selectUser(null) }) { Text("Закрыть") }
            }
        )
    }
}

@Composable
private fun UserCard(user: UserDto, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            val fullName = listOfNotNull(user.person?.lastName, user.person?.firstName)
                .joinToString(" ")
            if (fullName.isNotBlank()) {
                Text(fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            user.login?.let { Text("Логин: $it", style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant) }
            user.email?.let { Text(it, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary) }
        }
    }
}
