package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.ui.components.CustomButton
import ci.nsu.mobile.main.ui.components.TextFieldWithOptionalStar
import ci.nsu.mobile.main.viewmodel.registration.RegisterEvents
import ci.nsu.mobile.main.viewmodel.registration.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val datePickerState = rememberDatePickerState()
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            viewModel.registerEvent(RegisterEvents.CleanAll)
            onRegisterSuccess()
        }
    }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Регистрация", fontSize = 30.sp, modifier = Modifier.padding(top = 20.dp, bottom = 5.dp))

            Text("* - обязательное для заполнения поле", color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp))
                Text("Регистрация...")
            }

            TextFieldWithOptionalStar(
                value = state.lastName,
                onValueChange = { viewModel.registerEvent(RegisterEvents.SurnameChanged(it))},
                hasStar = true,
                placeholder = "Фамилия",
                isError = "LastName" in state.errorFields,
            )

            TextFieldWithOptionalStar(
                value = state.firstName,
                onValueChange = { viewModel.registerEvent(RegisterEvents.NameChanged(it))},
                hasStar = true,
                placeholder = "Имя",
                isError = "FirstName" in state.errorFields
            )

            TextFieldWithOptionalStar(
                value = state.middleName,
                onValueChange = { viewModel.registerEvent(RegisterEvents.PatronymicChanged(it))},
                hasStar = false,
                placeholder = "Отчество (при наличии)"
            )

            TextFieldWithOptionalStar(
                value = state.birthDate,
                onValueChange = { viewModel.registerEvent(RegisterEvents.BirthdayChanged(it))},
                hasStar = false,
                placeholder = "Дата рождения",
                readOnly = true,
                trailingIcon = {
                    IconButton({viewModel.registerEvent(RegisterEvents.DatePickerVisibilityChanged(!state.showDatePicker))}) {
                        Icon(
                            Icons.Filled.DateRange, "Выбрать дату рождения"
                        )
                    }
                }
            )
            if (state.showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = {
                        viewModel.registerEvent(RegisterEvents.DatePickerVisibilityChanged(false))
                    },
                    confirmButton = {
                        CustomButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val formattedDate = java.text.SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        java.util.Locale.getDefault()
                                    ).format(java.util.Date(millis))
                                    viewModel.registerEvent(RegisterEvents.BirthdayChanged(formattedDate))
                                }
                                viewModel.registerEvent(RegisterEvents.DatePickerVisibilityChanged(false))
                            },
                            title = "OK"
                        )
                    },
                    dismissButton = {
                        CustomButton(
                            onClick = {
                                viewModel.registerEvent(RegisterEvents.DatePickerVisibilityChanged(false))
                            },
                            title = "Отмена"
                        )
                    }
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.sizeIn(maxWidth = 350.dp)
                    )
                }
            }

            Column(Modifier.fillMaxWidth().padding(start = 33.dp)) {
                Text("Пол")
                Row(modifier = Modifier.fillMaxWidth()) {
                    state.genders.forEach { gender ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = state.gender == gender, onClick = {
                                viewModel.registerEvent(RegisterEvents.GenderChanged(gender))
                            })
                            Text(gender)
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = state.showDDMenu,
                onExpandedChange = { viewModel.registerEvent(RegisterEvents.MenuStateChanged(!state.showDDMenu)) },
            ) {
                TextField(
                    value = state.groupName,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Группа")},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.showDDMenu)
                    },
                    modifier = Modifier.menuAnchor(
                        ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true).padding(bottom = 20.dp).fillMaxWidth(0.81f)
                )
                ExposedDropdownMenu(
                    expanded = state.showDDMenu,
                    onDismissRequest = { viewModel.registerEvent(RegisterEvents.MenuStateChanged(false)) }
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
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            if (state.isLoadingGroups) {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 7.dp)
                    .size(30.dp))
                Text("Загрузка групп...")
            }

            TextFieldWithOptionalStar(
                value = state.login,
                onValueChange = { viewModel.registerEvent(RegisterEvents.LoginChanged(it))},
                hasStar = true,
                placeholder = "Логин",
                isError = "Login" in state.errorFields
            )
            TextFieldWithOptionalStar(
                value = state.password,
                onValueChange = { viewModel.registerEvent(RegisterEvents.PasswordChanged(it))},
                hasStar = true,
                placeholder = "Пароль",
                trailingIcon = {
                    val icon = if (state.passwordState)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff
                    val contentDescription = if (state.passwordState) "Показать пароль" else "Скрыть пароль"
                    IconButton({ viewModel.registerEvent(RegisterEvents.PasswordVisibilityChanged(!state.passwordState))}) {
                        Icon(icon, contentDescription)
                    }
                },
                isError = "Password" in state.errorFields,
                visualTransformation = if (!state.passwordState) PasswordVisualTransformation() else VisualTransformation.None
            )
            TextFieldWithOptionalStar(
                value = state.email,
                onValueChange = { viewModel.registerEvent(RegisterEvents.EmailChanged(it))},
                hasStar = true,
                placeholder = "Почта",
                isError = "Email" in state.errorFields
            )
            TextFieldWithOptionalStar(
                value = state.phoneNumber,
                onValueChange = { viewModel.registerEvent(RegisterEvents.PhoneNumberChanged(it))},
                hasStar = false,
                placeholder = "Телефон",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            CustomButton({

                viewModel.registerEvent(RegisterEvents.SubmitRegister)},
                "Зарегистрироваться"
            )

            if (state.errorMessage != null) {
                Text(state.errorMessage.toString(), color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
