@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val state = viewModel

    if (state.loginSuccess) {
        LaunchedEffect(Unit) { onLoginSuccess() }
    }
