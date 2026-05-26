package ci.nsu.mobile.main.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.TextField
import ci.nsu.mobile.main.ViewModel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextField(
            value = login,
            label = { Text("Login") },
            onValueChange = { login = it },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        )

        Button(
            onClick = {
                viewModel.login(login, password, onLoginSuccess)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Sign in")
        }
        Button(
            onClick = onNavigateToRegister,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("No account? Go to register")
        }
        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        }

        viewModel.error?.let {
            Text(it, color = Color.Red)
        }
    }
}