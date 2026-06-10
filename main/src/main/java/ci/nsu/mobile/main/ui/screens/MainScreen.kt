package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.mobile.main.data.models.DepositCalculation
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
    var selectedCalculation by remember { mutableStateOf<DepositCalculation?>(null) }

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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCalculation = calc }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${calc.initialAmount} ₽ → ${String.format("%.2f", calc.finalAmount)} ₽",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${formatDate(calc.calculationDate)}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Срок: ${calc.periodMonths} мес. под ${calc.interestRate}%",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Кнопка удаления
                        Text(
                            text = "☠\uFE0F",
                            modifier = Modifier
                                .clickable { depositViewModel.deleteCalculation(calc) }
                                .padding(8.dp),
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }

    // Диалог с деталями расчёта
    if (selectedCalculation != null) {
        AlertDialog(
            onDismissRequest = { selectedCalculation = null },
            title = { Text("Детали расчёта") },
            text = {
                Column {
                    Text("Стартовый взнос: ${selectedCalculation!!.initialAmount} ₽")
                    Text("Срок: ${selectedCalculation!!.periodMonths} мес.")
                    Text("Ставка: ${selectedCalculation!!.interestRate}%")
                    if (selectedCalculation!!.monthlyTopUp != null) {
                        Text("Пополнение: ${selectedCalculation!!.monthlyTopUp} ₽/мес.")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Итоговая сумма: ${String.format("%.2f", selectedCalculation!!.finalAmount)} ₽",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        "Начисленные проценты: ${String.format("%.2f", selectedCalculation!!.interestEarned)} ₽",
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Дата: ${formatDate(selectedCalculation!!.calculationDate)}", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                Button(onClick = { selectedCalculation = null }) {
                    Text("Закрыть")
                }
            }
        )
    }
}
