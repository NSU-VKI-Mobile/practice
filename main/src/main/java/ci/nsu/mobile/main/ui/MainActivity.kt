package ci.nsu.mobile.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.UsersViewModel
import ci.nsu.mobile.main.di.ViewModelFactory
import ci.nsu.mobile.main.data.model.User

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Users : Screen("users", "Пользователи", Icons.Filled.People)
    object History : Screen("history", "Мои расчёты", Icons.Filled.History)
    object NewCalc : Screen("new_calc", "Новый расчёт", Icons.Filled.Add)

    object Profile : Screen("profile/{userId}", "Профиль", null) {
        fun createRoute(userId: Long) = "profile/$userId"
    }
}

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by lazy { ViewModelFactory().create(AuthViewModel::class.java) }
    private val depositViewModel: DepositViewModel by lazy { ViewModelFactory().create(DepositViewModel::class.java) }
    private val usersViewModel: UsersViewModel by lazy { ViewModelFactory().create(UsersViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    depositViewModel: DepositViewModel,
    usersViewModel: UsersViewModel
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    var showRegister by remember { mutableStateOf(false) }


    LaunchedEffect(isLoggedIn) {
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
    val bottomScreens = listOf(Screen.Users, Screen.History, Screen.NewCalc)
    val username by authViewModel.currentUsername.collectAsState()
    val allUsers by usersViewModel.users.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Расчёт вкладов") },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                bottomScreens.forEach { screen ->
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
            startDestination = Screen.Users.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // В MainScaffold
            composable(Screen.Users.route) {
                UsersScreen(
                    viewModel = usersViewModel,
                    onUserSelected = { user -> // user имеет тип UserDto
                        navController.navigate(Screen.Profile.createRoute(user.userId))
                    }
                )
            }

            composable(
                route = Screen.Profile.route,
                arguments = listOf(navArgument("userId") { type = NavType.LongType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getLong("userId")

                // 🟢 Ищем пользователя среди allUsers (которые теперь UserDto)
                val user = allUsers.find { it.userId == userId }

                ProfileScreen(
                    user = user,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = depositViewModel,
                    currentUsername = username
                )
            }

            composable(Screen.NewCalc.route) {
                NewCalculationScreen(viewModel = depositViewModel)
            }
        }
    }
}