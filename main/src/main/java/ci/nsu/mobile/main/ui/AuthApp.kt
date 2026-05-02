package ci.nsu.mobile.main.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.model.GroupDto
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.model.details
import ci.nsu.mobile.main.data.model.displayName
import ci.nsu.mobile.main.domain.DepositResult

private val AppColorScheme = lightColorScheme(
    primary = Color(0xFF006C67),
    secondary = Color(0xFF7D5260),
    tertiary = Color(0xFF48607D),
    background = Color(0xFFF9FBFA),
    surface = Color(0xFFFFFFFF)
)

private enum class MainTab(
    val title: String,
    val marker: String
) {
    Users("Пользователи", "П"),
    History("Мои расчёты", "М"),
    NewCalculation("Новый расчёт", "Н")
}

private enum class CalculationStep {
    Step1,
    Step2,
    Result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthApp(
    viewModel: AuthViewModel,
    depositViewModel: DepositViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.currentUserId) {
        depositViewModel.setUserId(state.currentUserId)
    }

    MaterialTheme(colorScheme = AppColorScheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AnimatedContent(
                targetState = state.screen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "auth_screen"
            ) { screen ->
                when (screen) {
                    AuthScreen.Login,
                    AuthScreen.Register -> AuthScaffold(
                        state = state,
                        viewModel = viewModel
                    )

                    AuthScreen.Users -> MainAuthorizedScaffold(
                        authState = state,
                        authViewModel = viewModel,
                        depositViewModel = depositViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScaffold(
    state: AuthUiState,
    viewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (state.screen) {
                            AuthScreen.Login -> "Вход"
                            AuthScreen.Register -> "Регистрация"
                            AuthScreen.Users -> "Пользователи"
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state.screen) {
                AuthScreen.Login -> LoginScreen(
                    state = state,
                    onLoginChanged = viewModel::onLoginChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onLogin = viewModel::login,
                    onOpenRegister = viewModel::openRegister
                )

                AuthScreen.Register -> RegisterScreen(
                    state = state,
                    onFormChanged = viewModel::onRegisterFormChanged,
                    onRegister = viewModel::register,
                    onOpenLogin = viewModel::openLogin,
                    onReloadGroups = viewModel::loadGroups
                )

                AuthScreen.Users -> Unit
            }

            if (state.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainAuthorizedScaffold(
    authState: AuthUiState,
    authViewModel: AuthViewModel,
    depositViewModel: DepositViewModel
) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Users) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Приложение: Расчёт вкладов") },
                actions = {
                    TextButton(onClick = authViewModel::logout) {
                        Text("Выйти")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Text(tab.marker, fontWeight = FontWeight.Bold) },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "main_tab"
            ) { tab ->
                when (tab) {
                    MainTab.Users -> UsersTab(
                        state = authState,
                        onRefresh = authViewModel::loadUsers
                    )

                    MainTab.History -> HistoryTab(depositViewModel)

                    MainTab.NewCalculation -> NewCalculationTab(depositViewModel)
                }
            }

            if (authState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
private fun LoginScreen(
    state: AuthUiState,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onOpenRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Авторизация",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            MessageBlock(state)
            OutlinedTextField(
                value = state.login,
                onValueChange = onLoginChanged,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true,
                label = { Text("Логин") }
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                singleLine = true,
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Войти")
            }
            TextButton(
                onClick = onOpenRegister,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Нет аккаунта? Зарегистрироваться")
            }
        }
    }
}

@Composable
private fun RegisterScreen(
    state: AuthUiState,
    onFormChanged: (RegisterForm) -> Unit,
    onRegister: () -> Unit,
    onOpenLogin: () -> Unit,
    onReloadGroups: () -> Unit
) {
    val form = state.registerForm
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Новый пользователь",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        MessageBlock(state)
        OutlinedTextField(
            value = form.lastName,
            onValueChange = { onFormChanged(form.copy(lastName = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Фамилия") }
        )
        OutlinedTextField(
            value = form.firstName,
            onValueChange = { onFormChanged(form.copy(firstName = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Имя") }
        )
        OutlinedTextField(
            value = form.middleName,
            onValueChange = { onFormChanged(form.copy(middleName = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Отчество") }
        )
        OutlinedTextField(
            value = form.birthDate,
            onValueChange = { onFormChanged(form.copy(birthDate = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Дата рождения") },
            placeholder = { Text("YYYY-MM-DD") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = form.gender,
            onValueChange = { onFormChanged(form.copy(gender = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Пол") }
        )
        GroupDropdown(
            groups = state.groups,
            selectedGroup = form.group,
            enabled = !state.isLoading && state.groups.isNotEmpty(),
            onSelected = { onFormChanged(form.copy(group = it)) }
        )
        TextButton(
            onClick = onReloadGroups,
            enabled = !state.isLoading,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Обновить список групп")
        }
        OutlinedTextField(
            value = form.login,
            onValueChange = { onFormChanged(form.copy(login = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Логин") }
        )
        OutlinedTextField(
            value = form.password,
            onValueChange = { onFormChanged(form.copy(password = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = form.email,
            onValueChange = { onFormChanged(form.copy(email = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = form.phoneNumber,
            onValueChange = { onFormChanged(form.copy(phoneNumber = it)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            label = { Text("Телефон") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Button(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Зарегистрироваться")
        }
        TextButton(
            onClick = onOpenLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Уже есть аккаунт? Войти")
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersTab(
    state: AuthUiState,
    onRefresh: () -> Unit
) {
    var selectedUserId by rememberSaveable { mutableStateOf<Int?>(null) }

    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Всего: ${state.users.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedButton(
                    onClick = onRefresh,
                    enabled = !state.isLoading
                ) {
                    Text("Обновить")
                }
            }

            MessageBlock(
                state = state,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (state.users.isEmpty() && !state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Пользователи не найдены.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.users) { user ->
                        val stableId = user.userId ?: user.id ?: user.login.hashCode()
                        UserCard(
                            user = user,
                            expanded = selectedUserId == stableId,
                            onClick = {
                                selectedUserId = if (selectedUserId == stableId) null else stableId
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryTab(viewModel: DepositViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()
    var selectedCalculationId by rememberSaveable { mutableLongStateOf(0L) }

    PullToRefreshBox(
        isRefreshing = state.isHistoryRefreshing,
        onRefresh = viewModel::refreshHistory,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterPanel(
                filters = state.filters,
                error = state.filterError,
                onFiltersChanged = viewModel::updateFilters,
                onReset = viewModel::resetFilters
            )

            AnimatedVisibility(visible = selectedCalculationId > 0L) {
                val selectedFlow = androidx.compose.runtime.remember(selectedCalculationId) {
                    viewModel.getCalculationById(selectedCalculationId)
                }
                val selectedCalculation by selectedFlow.collectAsStateWithLifecycle(initialValue = null)
                selectedCalculation?.let { calculation ->
                    CalculationDetailCard(
                        calculation = calculation,
                        onClose = { selectedCalculationId = 0L },
                        onDelete = {
                            viewModel.deleteCalculation(calculation.id)
                            selectedCalculationId = 0L
                        }
                    )
                }
            }

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("История расчётов текущего пользователя пока пустая.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history, key = { it.id }) { item ->
                        CalculationCard(
                            calculation = item,
                            onClick = { selectedCalculationId = item.id },
                            onDelete = { viewModel.deleteCalculation(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewCalculationTab(viewModel: DepositViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var step by rememberSaveable { mutableStateOf(CalculationStep.Step1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "calculation_step"
        ) { currentStep ->
            when (currentStep) {
                CalculationStep.Step1 -> Step1Content(
                    state = state,
                    onAmountChange = viewModel::updateInitialAmount,
                    onMonthsChange = viewModel::updatePeriodMonths,
                    onNextClick = {
                        if (viewModel.validateStep1()) {
                            step = CalculationStep.Step2
                        }
                    },
                    onClearClick = viewModel::clearDraft
                )

                CalculationStep.Step2 -> Step2Content(
                    state = state,
                    onRateSelected = viewModel::updateSelectedRate,
                    onTopUpChange = viewModel::updateMonthlyTopUp,
                    onBackClick = { step = CalculationStep.Step1 },
                    onCalculateClick = {
                        if (viewModel.calculateResult()) {
                            step = CalculationStep.Result
                        }
                    }
                )

                CalculationStep.Result -> ResultContent(
                    result = state.result,
                    isSaved = state.isSaved,
                    saveMessage = state.saveMessage,
                    onSaveClick = viewModel::saveCurrentResult,
                    onNewClick = {
                        viewModel.clearDraft()
                        step = CalculationStep.Step1
                    },
                    onBackClick = { step = CalculationStep.Step2 }
                )
            }
        }
    }
}

@Composable
private fun FilterPanel(
    filters: DepositFilterState,
    error: String?,
    onFiltersChanged: (DepositFilterState) -> Unit,
    onReset: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Фильтры",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = filters.minAmount,
                onValueChange = { onFiltersChanged(filters.copy(minAmount = it)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Сумма от") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = filters.maxAmount,
                onValueChange = { onFiltersChanged(filters.copy(maxAmount = it)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Сумма до") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = filters.fromDate,
                onValueChange = { onFiltersChanged(filters.copy(fromDate = it)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Дата от") },
                placeholder = { Text("dd.MM.yyyy") }
            )
            OutlinedTextField(
                value = filters.toDate,
                onValueChange = { onFiltersChanged(filters.copy(toDate = it)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = { Text("Дата до") },
                placeholder = { Text("dd.MM.yyyy") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.weight(1f)
                )
            } ?: Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onReset) {
                Text("Сбросить")
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun Step1Content(
    state: DepositUiState,
    onAmountChange: (String) -> Unit,
    onMonthsChange: (String) -> Unit,
    onNextClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Основные параметры",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = onAmountChange,
            label = { Text("Стартовый взнос") },
            isError = state.step1Error != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.periodMonths,
            onValueChange = onMonthsChange,
            label = { Text("Срок вклада в месяцах") },
            isError = state.step1Error != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        state.step1Error?.let { ErrorText(it) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onClearClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Очистить")
            }
            Button(
                onClick = onNextClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Далее")
            }
        }
    }
}

@Composable
private fun Step2Content(
    state: DepositUiState,
    onRateSelected: (Double) -> Unit,
    onTopUpChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCalculateClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Дополнительные параметры",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                enabled = state.availableRates.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(state.selectedRate?.let { formatPercent(it) } ?: "Выберите ставку")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.availableRates.forEach { rate ->
                    DropdownMenuItem(
                        text = { Text(formatPercent(rate)) },
                        onClick = {
                            onRateSelected(rate)
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = state.monthlyTopUp,
            onValueChange = onTopUpChange,
            label = { Text("Ежемесячное пополнение") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        state.step2Error?.let { ErrorText(it) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }
            Button(
                onClick = onCalculateClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Рассчитать")
            }
        }
    }
}

@Composable
private fun ResultContent(
    result: DepositResult?,
    isSaved: Boolean,
    saveMessage: String?,
    onSaveClick: () -> Unit,
    onNewClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        if (result == null) {
            Text("Расчёт пока не выполнен.")
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InfoRow("Стартовый взнос", formatMoney(result.initialAmount))
                    InfoRow("Срок вклада", "${result.periodMonths} мес.")
                    InfoRow("Процентная ставка", formatPercent(result.interestRate))
                    InfoRow("Ежемесячное пополнение", formatTopUp(result.monthlyTopUp))
                    InfoRow("Итоговая сумма", formatMoney(result.finalAmount))
                    InfoRow("Начисленные проценты", formatMoney(result.interestEarned))
                }
            }
        }
        saveMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Назад")
            }
            Button(
                onClick = onSaveClick,
                enabled = result != null && !isSaved,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isSaved) "Сохранено" else "Сохранить")
            }
        }
        OutlinedButton(
            onClick = onNewClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Новый расчёт")
        }
    }
}

@Composable
private fun GroupDropdown(
    groups: List<GroupDto>,
    selectedGroup: GroupDto?,
    enabled: Boolean,
    onSelected: (GroupDto) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text(selectedGroup?.name?.takeIf { it.isNotBlank() } ?: "Выберите группу")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.name.ifBlank { "Группа #${group.id}" }) },
                    onClick = {
                        expanded = false
                        onSelected(group)
                    }
                )
            }
        }
    }
}

@Composable
private fun UserCard(
    user: UserDto,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = user.displayName(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            val details = user.details()
            if (details.isNotBlank()) {
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                InfoRow("ID", "${user.userId ?: user.id ?: "-"}")
                InfoRow("Роль", "${user.roleId ?: "-"}")
                InfoRow("Доступ", if (user.authAllowed == false) "запрещён" else "разрешён")
                user.person?.let { person ->
                    InfoRow("Дата рождения", person.birthDate.ifBlank { "-" })
                    InfoRow("Пол", person.gender.ifBlank { "-" })
                    InfoRow("Группа", person.groupName ?: person.groupId?.toString() ?: "-")
                }
            }
        }
    }
}

@Composable
private fun CalculationCard(
    calculation: DepositCalculation,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = formatDateTime(calculation.calculationDate),
                style = MaterialTheme.typography.labelMedium
            )
            Text("Стартовый взнос: ${formatMoney(calculation.initialAmount)}")
            Text(
                text = "Итоговая сумма: ${formatMoney(calculation.finalAmount)}",
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text("Удалить")
                }
            }
        }
    }
}

@Composable
private fun CalculationDetailCard(
    calculation: DepositCalculation,
    onClose: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Детали расчёта",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            InfoRow("Дата расчёта", formatDateTime(calculation.calculationDate))
            InfoRow("Стартовый взнос", formatMoney(calculation.initialAmount))
            InfoRow("Срок вклада", "${calculation.periodMonths} мес.")
            InfoRow("Процентная ставка", formatPercent(calculation.interestRate))
            InfoRow("Ежемесячное пополнение", formatTopUp(calculation.monthlyTopUp))
            InfoRow("Итоговая сумма", formatMoney(calculation.finalAmount))
            InfoRow("Начисленные проценты", formatMoney(calculation.interestEarned))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Закрыть")
                }
                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Удалить")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MessageBlock(
    state: AuthUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        state.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        state.infoMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium
    )
}
