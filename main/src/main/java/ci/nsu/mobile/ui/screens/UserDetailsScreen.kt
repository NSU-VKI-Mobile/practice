package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.data.model.UserDto

@Composable
fun UserDetailsScreen(
    user: UserDto,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Профиль пользователя",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("ID: ${user.userId}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Логин: ${user.login}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Email: ${user.email}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Телефон: ${user.phoneNumber}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Role ID: ${user.roleId}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Person ID: ${user.personId}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Разрешён вход: ${user.authAllowed}")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Создан: ${user.createdDate}")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Последний вход: ${
                user.lastLoginDate ?: "нет данных"
            }"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack
        ) {
            Text("Назад")
        }
    }
}