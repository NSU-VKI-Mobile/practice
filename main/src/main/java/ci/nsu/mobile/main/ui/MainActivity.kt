package ci.nsu.mobile.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.UsersViewModel
import ci.nsu.mobile.main.di.ViewModelFactory

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Users : Screen("users", "Пользователи", Icons.Filled.People)
    object History : Screen("history", "Мои расчёты", Icons.Filled.History)
    object NewCalc : Screen("new_calc", "Новый расчёт", Icons.Filled.Add)
}

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by lazy { ViewModelFactory().create(AuthViewModel::class.java) }
    private val depositViewModel: DepositViewModel by lazy { ViewModelFactory().create(DepositViewModel::class.java) }
    private val usersViewModel: UsersViewModel by lazy { ViewModelFactory().create(UsersViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Проверяем статус при холодном старте
        authViewModel.checkInitialAuthState()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(authViewModel, depositViewModel, usersViewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    depositViewModel: DepositViewModel, // <-- Убедитесь, что он передается
    usersViewModel: UsersViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    var showRegister by remember { mutableStateOf(false) }

    // 🟢 СЛЕДИМ ЗА ИЗМЕНЕНИЕМ СТАТУСА ВХОДА
    LaunchedEffect(isLoggedIn) {
        // Каждый раз когда isLoggedIn меняется (вход или выход),
        // обновляем userId в DepositViewModel
        depositViewModel.refreshUserId()
    }

    if (!isLoggedIn) {
        if (showRegister) {
            RegisterScreen(viewModel = authViewModel, onBackToLogin = { showRegister = false })
        } else {
            LoginScreen(viewModel = authViewModel, onNavigateToRegister = { showRegister = true })
        }
    } else {
        MainScaffold(navController, depositViewModel, usersViewModel, authViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    depositViewModel: DepositViewModel,
    usersViewModel: UsersViewModel,
    authViewModel: AuthViewModel
) {
    val screens = listOf(Screen.Users, Screen.History, Screen.NewCalc)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") },
                actions = {
                    IconButton(onClick = {
                        // 🟢 ВЫЗОВ LOGOUT
                        authViewModel.logout()
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.NewCalc.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Users.route) { UsersScreen(viewModel = usersViewModel) }
            composable(Screen.History.route) { HistoryScreen(viewModel = depositViewModel) }
            composable(Screen.NewCalc.route) { NewCalculationScreen(viewModel = depositViewModel) }
        }
    }
}