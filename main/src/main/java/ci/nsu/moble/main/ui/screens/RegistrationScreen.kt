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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(viewModel: RegisterViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    val groups by viewModel.groups.collectAsState()

    // Fields (and default values)
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val middleName by viewModel.middleName.collectAsState()
    val birthDate by viewModel.birthDate.collectAsState()
    val gender by viewModel.gender.collectAsState()
    val selectedGroup by viewModel.selectedGroup.collectAsState()
    val groupsState by viewModel.groupsState.collectAsState()

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

                        OutlinedTextField(value = lastName, onValueChange = { viewModel.updateField("lastName", it, viewModel.lastName) }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = firstName, onValueChange = { viewModel.updateField("firstName", it, viewModel.firstName)  }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = middleName, onValueChange = { viewModel.updateField("middleName", it, viewModel.middleName) }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth())

                        // --- Groups dropdown ---
                        val groupsState by viewModel.groupsState.collectAsState()
                        ExposedDropdownMenuBox(
                            // expanded state
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = !isExpanded }
                        ) {
                            // Field when not expanded
                            OutlinedTextField(
                                // group menu states names for ui
                                value = when (groupsState) {
                                    is GroupsState.Success -> selectedGroup?.name ?: "Выберите группу"
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
                                when (val state = groupsState) {
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
                                                    viewModel.updateField("selectedGroup", group, viewModel.selectedGroup)
                                                    isExpanded = false
                                                }
                                            )
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        }

                        OutlinedTextField(value = birthDate, onValueChange = { viewModel.updateField("birthDate", it, viewModel.birthDate) }, label = { Text("Дата рождения (ГГГГ-ММ-ДД)") }, modifier = Modifier.fillMaxWidth())

                        // --- Gender dropdown ---
                        ExposedDropdownMenuBox(
                            expanded = isGenderExpanded,
                            onExpandedChange = { isGenderExpanded = !isGenderExpanded }
                        ) {
                            OutlinedTextField(
                                value = genderOptions.find { it.second == gender }?.first ?: "Выберите пол",
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
                                            viewModel.updateField("gender", value, viewModel.gender)
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

                        OutlinedTextField(value = email, onValueChange = { viewModel.updateField("email", it, viewModel.email) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = phone, onValueChange = { viewModel.updateField("phone", it, viewModel.phone) }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = login, onValueChange = { viewModel.updateField("login", it, viewModel.login) }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.updateField("password", it, viewModel.password) },
                            label = { Text("Пароль") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                if (state.error != null) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
                }

                Button(
                    onClick = {
                        if (selectedGroup != null) {
                            viewModel.register(
                                login, password, email, phone,
                                firstName, lastName, middleName, birthDate, gender, selectedGroup!!.id,
                                onSuccess = onBack
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading && selectedGroup != null
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Зарегистрироваться")
                    }
                }
            }
        }
    }
}