package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.ui.viewmodel.MainViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    depositViewModel: DepositViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val uiState by mainViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.loadUsers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "Пользователи"
                            else -> "История расчётов"
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("deposit_menu") }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Новый расчёт")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            mainViewModel.logout()
                            navController.navigate("login") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Юзеры") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("История") },
                    icon = { Icon(Icons.Default.List, contentDescription = null) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> UsersListTab(uiState = uiState, onRetry = { mainViewModel.loadUsers() })
                1 -> HistoryTab(depositViewModel = depositViewModel)
            }
        }
    }
}

@Composable
fun UsersListTab(uiState: ci.nsu.mobile.main.ui.viewmodel.MainUiState, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) { Text("Повторить") }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.users) { user ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = user.login, style = MaterialTheme.typography.titleMedium)
                                Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                                Text(text = "ID пользователя: ${user.id}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Последний вход: ${user.lastLoginDate ?: "никогда"}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryTab(depositViewModel: DepositViewModel) {
    val history by depositViewModel.historyState.collectAsState()

    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("История расчётов пуста")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(history) { calc ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Вклад: ${calc.initialAmount} руб.", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Срок: ${calc.periodMonths} мес. под ${calc.interestRate}%", style = MaterialTheme.typography.bodyMedium)
                        if (calc.monthlyTopUp != null) {
                            Text(text = "Пополнение: ${calc.monthlyTopUp} руб./мес.", style = MaterialTheme.typography.bodySmall)
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Text(text = "Доход: %.2f руб.".format(calc.interestEarned), color = MaterialTheme.colorScheme.primary)
                        Text(text = "Итог: %.2f руб.".format(calc.finalAmount), style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        // Используем существующий метод formatDate из HistoryScreen.kt
                        Text(text = formatDate(calc.calculationDate), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}