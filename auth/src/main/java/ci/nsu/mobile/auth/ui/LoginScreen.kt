package ci.nsu.mobile.auth.ui

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.auth.utils.NotificationHelper
import ci.nsu.mobile.auth.viewmodel.AuthState
import ci.nsu.mobile.auth.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showQrScanner by remember { mutableStateOf(false) }

    val loginState by authViewModel.loginState.collectAsState()

    // Инициализация звуков
    LaunchedEffect(Unit) {
        NotificationHelper.initSounds(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            NotificationHelper.releaseSounds()
        }
    }

    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            onLoginSuccess()
            authViewModel.resetStates()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вход в систему",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (login.isNotBlank() && password.isNotBlank()) {
                    authViewModel.login(login, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is AuthState.Loading
        ) {
            if (loginState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Войти")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { showQrScanner = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📷 Авторизация через QR-код")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Нет аккаунта? Зарегистрироваться")
        }

        when (val state = loginState) {
            is AuthState.Error -> {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {}
        }
    }

    // QR-сканер
    if (showQrScanner) {
        QrScannerScreen(
            onQrScanned = { scannedLogin, scannedPassword ->
                login = scannedLogin
                password = scannedPassword
                showQrScanner = false
                NotificationHelper.playSuccessSound()
                NotificationHelper.showSuccessNotification(context)
            },
            onTimeout = {
                showQrScanner = false
                NotificationHelper.playFailureSound()
                NotificationHelper.showFailureNotification(context)
            },
            onClose = {
                showQrScanner = false
            }
        )
    }
}