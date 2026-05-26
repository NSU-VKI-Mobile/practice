package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Card
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TopAppBar
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import ci.nsu.mobile.main.Screens.DepositAddStages.FirstStageScreen
import ci.nsu.mobile.main.Screens.DepositAddStages.SecondStageScreen
import ci.nsu.mobile.main.Screens.DepositAddStages.ResultStageScreen
import ci.nsu.mobile.main.Screens.HistoryStageScreen
import ci.nsu.mobile.main.Screens.Auth.LoginScreen
import ci.nsu.mobile.main.Screens.Auth.RegistrationScreen
import ci.nsu.mobile.main.Screens.Users.UsersScreen
import ci.nsu.mobile.main.Token.TokenManager
import ci.nsu.mobile.main.Token.UserManager
import ci.nsu.mobile.main.ViewModel.AuthViewModel
import ci.nsu.mobile.main.ViewModel.DepositViewModel
import ci.nsu.mobile.main.DI.*
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    private lateinit var serviceLocator: ServiceLocator
    private lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        UserManager.init(this)

        serviceLocator = ServiceLocator(applicationContext)
        viewModelFactory = ViewModelFactory(serviceLocator)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                AppNavigation(viewModelFactory)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModelFactory: ViewModelFactory) {
    var refreshKey by remember { mutableStateOf(0) }

    var isLoggedIn by remember {
        mutableStateOf(TokenManager.isLoggedIn() && UserManager.isLoggedIn())
    }

    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

    if (!isLoggedIn) {
        AuthNavHost(
            navController = rememberNavController(),
            authViewModel = authViewModel,
            onLoginSuccess = {
                isLoggedIn = true
                refreshKey++
            }
        )
    } else {
        key(refreshKey) {
            MainAppNavHost(
                navController = rememberNavController(),
                authViewModel = authViewModel,
                viewModelFactory = viewModelFactory,
                onLogout = {
                    isLoggedIn = false
                    refreshKey++
                }
            )
        }
    }
}

@Composable
fun AuthNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = onLoginSuccess,
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegistrationScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                navController.popBackStack()
            },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    viewModelFactory: ViewModelFactory,
    onLogout: () -> Unit
) {
    val depositViewModel: DepositViewModel = viewModel(factory = viewModelFactory)

    LaunchedEffect(Unit) {
        depositViewModel.loadHistoryForCurrentUser()
        authViewModel.loadUsers()
    }
    DisposableEffect(Unit) {
        onDispose {

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculation of deposits") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Text("Leave")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == "calculate",
                    onClick = { navController.navigate("calculate") },
                    icon = { Text("Icon1")},
                    label = { Text("Calculation") }
                )
                NavigationBarItem(
                    selected = currentRoute == "history",
                    onClick = { navController.navigate("history") },
                    icon = { Text("Icon2") },
                    label = { Text("History") }
                )
                NavigationBarItem(
                    selected = currentRoute == "users",
                    onClick = { navController.navigate("users") },
                    icon = { Text("Icon3") },
                    label = { Text("Users") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "calculate",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("calculate") {
                CalculationNavHost(depositViewModel)
            }
            composable("history") {
                HistoryStageScreen(navController, depositViewModel)
            }
            composable("users") {
                UsersScreen(viewModel = authViewModel)
            }
        }
    }
}

@Composable
fun CalculationNavHost(depositViewModel: DepositViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "first_stage") {
        composable("first_stage") {
            FirstStageScreen(navController, depositViewModel)
        }
        composable("second_stage") {
            SecondStageScreen(navController, depositViewModel)
        }
        composable("result") {
            ResultStageScreen(navController, depositViewModel)
        }
    }
}