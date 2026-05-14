package ci.nsu.mobile.main.presentation.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.DepositApplication

@Composable
fun UsersScreen() {
    val application = LocalContext.current.applicationContext as DepositApplication
    val viewModel: UsersViewModel = viewModel(
        factory = UsersViewModelFactory(application.locator.authRepository)
    )

    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.error!!)
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.users) { user ->
                    Card {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                text = "${user.person?.lastName ?: ""} ${user.person?.firstName ?: ""} ${user.person?.middleName ?: ""}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = "Логин: ${user.login}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}