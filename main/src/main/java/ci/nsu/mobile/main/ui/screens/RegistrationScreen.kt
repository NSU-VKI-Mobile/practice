package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.ui.components.CustomButton
import ci.nsu.mobile.main.viewmodel.RegistrationViewModel
import ci.nsu.mobile.main.viewmodel.state.RegisterEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navTo: (String) -> Unit,
    viewModel: RegistrationViewModel,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.registerEvent(RegisterEvents.CleanAll)
            onRegisterSuccess()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var selectedDate by remember { mutableStateOf<Long?>(null) }
        var showDialog by remember { mutableStateOf(false) }
        var showMenu by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        TextField(
            value = state.lastName,
            onValueChange = { viewModel.registerEvent(RegisterEvents.SurnameChanged(it))},
            Modifier.padding(bottom = 10.dp),
            placeholder = { Text("Фамилия")}
        )
        TextField(
            value = state.firstName,
            onValueChange = { viewModel.registerEvent(RegisterEvents.NameChanged(it))},
            Modifier.padding(bottom = 10.dp),
            placeholder = { Text("Имя")}
        )
        TextField(
            value = state.middleName ?: "",
            onValueChange = { viewModel.registerEvent(RegisterEvents.PatronymicChanged(it))},
            Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = state.birthDate ?: "",
            onValueChange = { viewModel.registerEvent(RegisterEvents.BirthdayChanged(it))},
            Modifier.padding(bottom = 10.dp),
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Выбрать дату"
                    )
                }
            })
        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    CustomButton(
                        onClick = {
                            viewModel.registerEvent(RegisterEvents.BirthdayChanged(datePickerState.selectedDateMillis.toString()))
                            showDialog = false
                        },
                        "OK")
                },
                dismissButton = {
                    CustomButton(
                        onClick = { showDialog = false },
                        "Cancel")
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.sizeIn(maxWidth = 350.dp)
                )
            }
        }
            Row(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = state.radioButtonsState, onClick = {
                        viewModel.registerEvent(RegisterEvents.RBStateChanged(true))
                    })
                    Text("Мужской")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = !state.radioButtonsState, onClick = {
                        viewModel.registerEvent(RegisterEvents.RBStateChanged(false))

                    })
                    Text("Женский")
                }
            }
            Box() {
                TextField(
                    value = state.groupName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.registerEvent(RegisterEvents.MenuStateChanged(true)) }) {
                            Icon(
                                Icons.Default.KeyboardArrowDown, contentDescription = "Выбрать группу"
                            )
                        }
                    },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                DropdownMenu(
                    expanded = state.showDDMenu,
                    onDismissRequest = { viewModel.registerEvent(RegisterEvents.MenuStateChanged(false)) },
                ) {
                    state.groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.groupName) },
                            onClick = {
                                viewModel.registerEvent(
                                    RegisterEvents.GroupChanged(group.groupId)
                                )
                                viewModel.registerEvent(RegisterEvents.MenuStateChanged(false))
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            TextField(
                state.login,
                onValueChange = { viewModel.registerEvent(RegisterEvents.LoginChanged(it))},
                Modifier.padding(bottom = 10.dp)
            )
            TextField(
                state.password,
                onValueChange = { viewModel.registerEvent(RegisterEvents.PasswordChanged(it))},
                Modifier.padding(bottom = 10.dp)
            )
            TextField(
                state.email,
                onValueChange = { viewModel.registerEvent(RegisterEvents.EmailChanged(it))},
                Modifier.padding(bottom = 10.dp)
            )
            TextField(
                state.phoneNumber ?: "",
                onValueChange = { viewModel.registerEvent(RegisterEvents.PhoneNumberChanged(it))},
                Modifier.padding(bottom = 10.dp)
            )
            CustomButton({ viewModel.registerEvent(RegisterEvents.SubmitRegister) }, "Зарегистрироваться")
        }
    }
