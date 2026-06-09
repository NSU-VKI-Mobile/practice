package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import ci.nsu.mobile.main.viewmodel.AuthViewModel

@Composable
fun HomeScreen(vm: AuthViewModel) {

    val users by vm.users.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    LazyColumn {

        items(users) { user ->
            Text("${user.login} | ${user.email}")
        }
    }
}