package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import ci.nsu.mobile.data.db.DepositCalculation
import ci.nsu.mobile.ui.viewmodel.AuthViewModel
import ci.nsu.mobile.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.utils.TokenManager
import ci.nsu.mobile.data.model.UserDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authVm: AuthViewModel,
    depositVm: DepositViewModel,
    userLogin: String
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("users") },
                    label = { Text("Пользователи") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    label = { Text("Расчёты") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("new") },
                    label = { Text("Новый") },
                    icon = {}
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "users",
            modifier = Modifier.padding(padding)
        ) {
            composable("users") {
                UsersScreen(
                    vm = authVm,
                    onUserClick = { user ->

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("user", user)

                        navController.navigate("user_details")
                    }
                )
            }

            composable("user_details") {

                val user = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<UserDto>("user")

                if (user != null) {
                    UserDetailsScreen(
                        user = user,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable("history") {
                CalculationsScreen(
                    vm = depositVm,
                    userLogin = TokenManager.userLogin,
                    onOpen = {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("calc", it)
                        navController.navigate("details")
                    }
                )
            }

            composable("new") {
                NewCalculationScreen(
                    vm = depositVm,
                    userLogin = TokenManager.userLogin,
                    onDone = { navController.navigate("history") }
                )
            }

            composable("details") {
                val item = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<DepositCalculation>("calc")

                if (item != null) {
                    CalculationDetailsScreen(
                        item = item,
                        vm = depositVm,
                        userLogin = TokenManager.userLogin,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}