package ci.nsu.mobile.main.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ci.nsu.mobile.auth.ui.AuthViewModel
import ci.nsu.mobile.auth.ui.screens.UsersTab
import ci.nsu.mobile.calculations.ui.DepositViewModel
import ci.nsu.mobile.calculations.ui.screens.DepositsTab
import ci.nsu.mobile.calculations.ui.screens.NewCalculationFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    authVm: AuthViewModel,
    depositVm: DepositViewModel,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title   = { Text("Расчёт вкладов") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick  = { selectedTab = 0 },
                    icon     = { Icon(Icons.Default.Person, null) },
                    label    = { Text("Пользователи") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick  = { selectedTab = 1 },
                    icon     = { Icon(Icons.Default.List, null) },
                    label    = { Text("Мои расчёты") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick  = { selectedTab = 2 },
                    icon     = { Icon(Icons.Default.Add, null) },
                    label    = { Text("Новый расчёт") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> UsersTab(authVm)       // из модуля :auth
                1 -> DepositsTab(depositVm) // из модуля :calculations
                2 -> NewCalculationFlow(depositVm) // из модуля :calculations
            }
        }
    }
}