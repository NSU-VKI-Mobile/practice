package ci.nsu.mobile.main.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ci.nsu.mobile.main.vm.DepositsViewModel
import ci.nsu.mobile.main.vm.LoginAndRegViewModel

sealed class Screen(val route: String) {
    object Input1 : Screen("input1")
    object Input2 : Screen("input2")
    object Calc : Screen("calc")
    object HistoryCalc : Screen("historyCalc")
    object UserList: Screen("userList")
}


@Composable
fun MainScreen(
    navController: NavController,
    depositsViewModel: DepositsViewModel,
    authViewModel: LoginAndRegViewModel
){

}