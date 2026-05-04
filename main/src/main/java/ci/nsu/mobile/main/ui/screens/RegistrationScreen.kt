package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.navigation.Screens
import ci.nsu.mobile.main.ui.components.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navTo: (String) -> Unit) {
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
            "surname",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp)
        )
        TextField(
            "name",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp)
        )
        TextField(
            "lastName",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp)
        )
        TextField(
            value = selectedDate.toString(),
            onValueChange = {selectedDate},
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
                            selectedDate = datePickerState.selectedDateMillis
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
                    RadioButton(selected = false, onClick = {})
                    Text("Мужской")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = true, onClick = {})
                    Text("Женский")
                }
            }
            //todo добавить иконку, по клику на которую открывается dropDownMenu
            TextField(
                "группа",
                onValueChange = { showMenu = true},

                Modifier.padding(bottom = 10.dp),
                trailingIcon = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Выбрать группу"
                        )
                    }
                }
            )
            TextField(
                "login",
                onValueChange = {},
                Modifier.padding(bottom = 10.dp)
            )
            TextField(
                "password",
                onValueChange = {},
                Modifier.padding(bottom = 10.dp)
            )
            TextField(
                "email",
                onValueChange = {},
                Modifier.padding(bottom = 10.dp)
            )
            TextField(
                "phone",
                onValueChange = {},
                Modifier.padding(bottom = 10.dp)
            )
            CustomButton({ navTo(Screens.UsersScreen.route) }, "Зарегистрироваться")
        }
    }