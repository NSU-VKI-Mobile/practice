package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(vm: AuthViewModel, navController: NavController) {

    LaunchedEffect(Unit) {
        vm.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Пользователи")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(vm.users) { user ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {

                            Text("👤 ${user.login}")
                            Text("📧 ${user.email}")
                            Text("📱 ${user.phoneNumber}")
                            Text("🎭 Role: ${user.roleId}")
                            Text("🆔 ID: ${user.userId}")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    vm.logout()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выйти")
            }
        }
    }
}