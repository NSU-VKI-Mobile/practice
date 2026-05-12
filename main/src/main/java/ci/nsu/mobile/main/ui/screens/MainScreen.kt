package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.vm.DepositsViewModel
import ci.nsu.mobile.main.vm.LoginAndRegViewModel

sealed class Screen(val route: String) {
    object Input1 : Screen("input1")
    object Input2 : Screen("input2")
    object Calc : Screen("calc")
    object HistoryCalc : Screen("historyCalc")
    object UserList: Screen("userList")
}

sealed class UseIcons(val icon: ImageVector){
    object UserList : UseIcons(Icons.Default.Person)
    object HistoryCalc : UseIcons(Icons.Default.Home)
    object AddCalc : UseIcons(Icons.Default.Add)
    object Back : UseIcons(Icons.Filled.ArrowBack)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    depositsViewModel: DepositsViewModel,
    authViewModel: LoginAndRegViewModel,
    onBackClick : () -> Unit
){
    var selectedItem by remember { mutableIntStateOf(0) }

    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = UseIcons.Back.icon,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Blue, titleContentColor = Color.White)
        )
    }, bottomBar = {
        NavigationBar {
            NavigationBarItem(
                icon = { Icon(imageVector = UseIcons.UserList.icon, contentDescription = stringResource(R.string.user_list)) },
                label = { Text(stringResource(R.string.user_list)) },
                selected = selectedItem == 0,

                onClick = {
                    navController.navigate(Screen.UserList.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    selectedItem = 0
                })
            NavigationBarItem(
                icon = { Icon(imageVector = UseIcons.HistoryCalc.icon, contentDescription = stringResource(R.string.history_calc)) },
                label = { Text(stringResource(R.string.history_calc)) },
                selected = selectedItem == 1,

                onClick = {
                    navController.navigate(Screen.HistoryCalc.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    selectedItem = 1
                })
            NavigationBarItem(
                icon = { Icon(imageVector = UseIcons.AddCalc.icon, contentDescription = stringResource(R.string.add_deposit)) },
                label = { Text(stringResource(R.string.add_deposit)) },
                selected = selectedItem == 2,
                onClick = {
                    navController.navigate(Screen.Input1.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    selectedItem = 2
                })
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.UserList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.UserList.route) {
                UserListScreen(authViewModel)
            }
            composable(Screen.HistoryCalc.route) {
                HistoryCalcScreen(depositsViewModel)
            }
            composable(Screen.Input1.route) {
                Input1Screen(
                    onNextClick = { navController.navigate(Screen.Input2.route) },
                    viewModel = depositsViewModel
                )
            }
            composable(Screen.Input2.route) {
                Input2Screen(
                    onBackClick = { navController.popBackStack() },
                    onCalcClick = { navController.navigate(Screen.Calc.route) },
                    viewModel = depositsViewModel
                )
            }
            composable(Screen.Calc.route) {
                CalcScreen(
                    onMainClick = { navController.popBackStack(Screen.Input1.route, inclusive = false) },
                    viewModel = depositsViewModel
                )
            }
        }
    }
}