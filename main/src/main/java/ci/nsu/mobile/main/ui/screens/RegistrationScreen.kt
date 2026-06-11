@Composable
fun RegistrationScreen(onRegistrationSuccess: () -> Unit, onBack: () -> Unit) {
    val viewModel: RegistrationViewModel = viewModel()