// Task_6: Экран списка пользователей — после входа, с кнопкой "Выход".
package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.moble.main.ui.MainViewModel

@Composable
fun UsersScreen(viewModel: MainViewModel) {
    val state by viewModel.usersState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Пользователи", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Выход", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val errorText = state.error
        if (errorText != null) {
            Text(errorText, color = androidx.compose.ui.graphics.Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (state.loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.users, key = { it.id }) { user ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${user.lastName ?: ""} ${user.firstName ?: ""}",
                            fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Text("Логин: ${user.login}", fontSize = 14.sp,
                            color = androidx.compose.ui.graphics.Color.Gray)
                        user.email?.let {
                            Text("Email: $it", fontSize = 14.sp,
                                color = androidx.compose.ui.graphics.Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
