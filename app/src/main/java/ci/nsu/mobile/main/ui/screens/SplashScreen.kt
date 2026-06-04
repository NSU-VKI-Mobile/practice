package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ci.nsu.mobile.domain.interfaces.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    authManager: AuthManager,
    onAuthenticated: () -> Unit,
    onUnauthenticated: () -> Unit
) {
    var isChecking by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val isLoggedIn = withContext(Dispatchers.IO) {
            authManager.isLoggedIn()
        }
        isChecking = false

        if (isLoggedIn) {
            onAuthenticated()
        } else {
            onUnauthenticated()
        }
    }

    if (isChecking) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}