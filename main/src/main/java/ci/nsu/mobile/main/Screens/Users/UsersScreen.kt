package ci.nsu.mobile.main.Screens.Users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.Data.DataModels.UserDto
import ci.nsu.mobile.main.ViewModel.AuthViewModel

@Composable
fun UsersScreen(
    viewModel: AuthViewModel,
    onUserClick: (UserDto) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Users",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }
        else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.users) { user ->
                    UserCard(
                        user = user,
                        onClick = { onUserClick(user) }
                    )
                }
            }
        }

        viewModel.error?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun UserCard(
    user: UserDto,
    onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "${user.login}",
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${user.email}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (expanded) {
                DetailRow(label = "ID", value = "${user.id}")
                DetailRow(label = "Login", value = user.login)
                DetailRow(label = "Email", value = user.email)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = value,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}