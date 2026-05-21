package ci.nsu.mobile.main.units.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onLoginSuccses: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(
            authRepository = remember { /*получить из Application*/}
        )
    )
) {
}