package ci.nsu.mobile.auth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import ci.nsu.mobile.auth.ui.AuthViewModel

@Composable
fun UsersTab(vm: AuthViewModel) {
    LaunchedEffect(Unit) { vm.loadUsers() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        if (vm.isLoading)
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.users) { user ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text("ID: ${user.id}",  style = MaterialTheme.typography.labelSmall)
                        Text(user.login,         style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}