@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") { popUpTo(0) } },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegistrationScreen(
                onRegistrationSuccess = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(onLogout = {
                TokenManager.clear()
                navController.navigate("login") { popUpTo(0) }
            })
        }
    }
}