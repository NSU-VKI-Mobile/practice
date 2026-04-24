package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import ci.nsu.mobile.main.di.ServiceLocator

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val factory = ViewModelFactory(ServiceLocator.authRepository, ServiceLocator.depositRepository)
    val authViewModel: AuthViewModel = viewModel(factory = factory)

    val startDest = if (authViewModel.isUserLoggedIn) "main_flow" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("main_flow") {
            MainContainerScreen(
                rootNavController = navController,
                factory = factory,
                authViewModel = authViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen(rootNavController: NavController, factory: ViewModelFactory, authViewModel: AuthViewModel) {
    val bottomNavController = rememberNavController()






    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Приложение: Расчёт вкладов") },
                actions = {
                    TextButton(onClick = {
                        authViewModel.logout()
                        rootNavController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }) { Text("Выход", color = MaterialTheme.colorScheme.error) }
                }
            )
        },
        bottomBar = { BottomNavigationBar(bottomNavController) }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "users",
            modifier = Modifier.padding(padding)
        ) {
            composable("users") {
                UsersScreen(authViewModel)
            }
            composable("history") {
                val depositViewModel: DepositViewModel = viewModel(factory = factory)
                HistoryScreen(depositViewModel)
            }
            navigation(startDestination = "step_one", route = "new_deposit") {
                composable("step_one") { backStackEntry ->
                    val parentEntry = remember(backStackEntry) { bottomNavController.getBackStackEntry("new_deposit") }
                    val depositViewModel: DepositViewModel = viewModel(parentEntry, factory = factory)
                    StepOneScreen(bottomNavController, depositViewModel)
                }
                composable("step_two") { backStackEntry ->
                    val parentEntry = remember(backStackEntry) { bottomNavController.getBackStackEntry("new_deposit") }
                    val depositViewModel: DepositViewModel = viewModel(parentEntry, factory = factory)
                    StepTwoScreen(bottomNavController, depositViewModel)
                }
                composable("result") { backStackEntry ->
                    val parentEntry = remember(backStackEntry) { bottomNavController.getBackStackEntry("new_deposit") }
                    val depositViewModel: DepositViewModel = viewModel(parentEntry, factory = factory)
                    ResultScreen(bottomNavController, depositViewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Triple("users", "Пользователи", Icons.Default.Person),
        Triple("history", "Мои расчёты", Icons.Default.List),
        Triple("new_deposit", "Новый расчёт", Icons.Default.Add)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { (route, title, icon) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(title) },
                selected = currentRoute?.startsWith(route) == true ||
                        (route == "new_deposit" && currentRoute in listOf("step_one", "step_two", "result")),
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}