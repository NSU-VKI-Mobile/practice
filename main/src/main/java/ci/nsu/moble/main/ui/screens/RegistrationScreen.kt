package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.viewmodel.RegisterViewModel
import ci.nsu.moble.main.viewmodel.states.GroupsState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(onBack: () -> Unit) {

    val viewModel: RegisterViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var isExpanded by remember { mutableStateOf(false) }
    var isGenderExpanded by remember { mutableStateOf(false) }

    val genderOptions = listOf("Мужской" to "MALE", "Женский" to "FEMALE")


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Регистрация") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // --- Personal data ---
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Личные данные", style = MaterialTheme.typography.titleMedium)

                        // Last Name
                        OutlinedTextField(
                            value = uiState.lastName,
                            onValueChange = { newValue ->
                                viewModel.updateField("lastName", newValue) { it.copy(lastName = newValue) }
                                            },
                            label = { Text("Фамилия") },
                            modifier = Modifier.fillMaxWidth())

                        // First name
                        OutlinedTextField(
                            value = uiState.firstName,
                            onValueChange = { newValue ->
                                viewModel.updateField("firstName", newValue) { it.copy(firstName = newValue) }
                            },
                            label = { Text("Имя") },
                            modifier = Modifier.fillMaxWidth())

                        // Middle name
                        OutlinedTextField(
                            value = uiState.middleName,
                            onValueChange = { newValue ->
                                viewModel.updateField("middleName", newValue) { it.copy(middleName = newValue) }
                            },
                            label = { Text("Отчество") },
                            modifier = Modifier.fillMaxWidth())

                        // --- Groups dropdown ---
                        //val groupsState by uiState.groupsState.collectAsState()
                        ExposedDropdownMenuBox(
                            // expanded state
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = !isExpanded }
                        ) {
                            // Field when not expanded
                            OutlinedTextField(
                                // group menu states names for ui
                                value = when (val groupsState = uiState.groupsState) {
                                    is GroupsState.Success -> uiState.selectedGroup?.name ?: "Выберите группу"
                                    is GroupsState.Loading -> "Загрузка..."
                                    is GroupsState.Error -> "Ошибка загрузки"
                                    else -> "Выберите группу"
                                },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Группа") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth()
                            )
                            // Expanded menu
                            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                                // handling states
                                when (val state = uiState.groupsState) {
                                    is GroupsState.Loading -> {
                                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                    }
                                    is GroupsState.Error -> {
                                        DropdownMenuItem(
                                            text = { Text("Ошибка. Попробовать снова?") },
                                            onClick = { viewModel.loadGroups() } // try to load groups again
                                        )
                                    }
                                    is GroupsState.Success -> {
                                        if (state.data.isEmpty()) {
                                            DropdownMenuItem(text = { Text("Список пуст") }, onClick = {})
                                        }
                                        state.data.forEach { group ->
                                            DropdownMenuItem(
                                                text = { Text(group.name) },
                                                onClick = {
                                                    viewModel.updateField("selectedGroup", group) { it.copy(selectedGroup = group) }
                                                    isExpanded = false
                                                }
                                            )
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        }

                        // Birth Date
                        OutlinedTextField(
                            value = uiState.birthDate,
                            onValueChange = { newValue ->
                                viewModel.updateField("birthDate", newValue) { it.copy(birthDate = newValue) }
                            },
                            label = { Text("Дата рождения (ГГГГ-ММ-ДД)") },
                            modifier = Modifier.fillMaxWidth())

                        // --- Gender dropdown ---
                        ExposedDropdownMenuBox(
                            expanded = isGenderExpanded,
                            onExpandedChange = { isGenderExpanded = !isGenderExpanded }
                        ) {
                            OutlinedTextField(
                                value = genderOptions.find { it.second == uiState.gender }?.first ?: "Выберите пол",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Пол") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGenderExpanded) },
                                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = isGenderExpanded,
                                onDismissRequest = { isGenderExpanded = false }
                            ) {
                                genderOptions.forEach { (label, value) ->
                                    DropdownMenuItem(
                                        text = { Text(label) },
                                        onClick = {
                                            viewModel.updateField("gender", value) { it.copy(gender = value) }
                                            isGenderExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // --- Account details ---
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Данные аккаунта", style = MaterialTheme.typography.titleMedium)

                        // Email
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = { newValue ->
                                viewModel.updateField("email", newValue) { it.copy(email = newValue) }
                            },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth())

                        // Phone
                        OutlinedTextField(
                            value = uiState.phone,
                            onValueChange = { newValue ->
                                viewModel.updateField("phone", newValue) { it.copy(phone = newValue) }
                            },
                            label = { Text("Телефон") },
                            modifier = Modifier.fillMaxWidth())

                        // Login
                        OutlinedTextField(
                            value = uiState.login,
                            onValueChange = { newValue ->
                                viewModel.updateField("login", newValue) { it.copy(login = newValue) }
                            },
                            label = { Text("Логин") },
                            modifier = Modifier.fillMaxWidth())

                        // Password
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { newPassword ->
                                viewModel.updateField("password", newPassword) { state ->
                                    state.copy(password = newPassword)
                                }
                            },
                            label = { Text("Пароль") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                // Show error message if it exists in the UI state
                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Determine if the form can be submitted
                val isGroupSelected = uiState.selectedGroup != null

                Button(
                    onClick = {
                        if (isGroupSelected) {
                            // Call refactored ViewModel method without passing all fields manually
                            viewModel.register(onSuccess = onBack)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    // Disable button during network requests or if group is missing
                    enabled = !uiState.isLoading && isGroupSelected
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Зарегистрироваться")
                    }
                }
            }
        }
    }
}