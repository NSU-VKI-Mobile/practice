package ci.nsu.mobile.main.units.ui.screens.register

import android.R
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.trace
import androidx.hilt.navigation.compose.hiltViewModel
import ci.nsu.mobile.main.units.ui.screens.login.LoginViewModel
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegistrationSuccess: () -> Unit
) {
    LaunchedEffect(viewModel.registrationSuccess) {
        if(viewModel.registrationSuccess) onRegistrationSuccess()
    }
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium)

        Text("Пол", style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = viewModel.selectedGender == "male",
                onClick = {viewModel.selectedGender = "male"}
            )
            Text("Мужской")
            RadioButton(
                selected = viewModel.selectedGender == "female",
                onClick = { viewModel.selectedGender = "female"}
            )
            Text("Женский")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(viewModel.firstName, {viewModel.firstName = it}, label = {Text("Имя")})
        OutlinedTextField(viewModel.lastName, {viewModel.lastName = it}, label = {Text("Фамилия")})
        OutlinedTextField(viewModel.middleName, {viewModel.middleName = it}, label = {Text("Отчество")})
        OutlinedTextField(viewModel.selectedGender, {viewModel.selectedGender = it}, label = {Text("Пол")})

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it}
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                value = viewModel.groups.firstOrNull{it.id == viewModel.selectedGroupId}?.name?:"",
                onValueChange = {},
                readOnly = true,
                label = {Text("Группа")},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                viewModel.groups.forEach { group ->
                    DropdownMenuItem(
                        text = {Text(group.name)},
                        onClick = {
                            viewModel.selectedGroupId = group.id
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(viewModel.login, {viewModel.login = it}, label = {Text("Логин")})
        OutlinedTextField(viewModel.password, {viewModel.password = it}, label = {Text("Пароль")})
        OutlinedTextField(viewModel.email, {viewModel.email = it}, label = {Text("Почта")})
        OutlinedTextField(viewModel.phone, {viewModel.phone = it}, label = {Text("Телефон")})

        if(viewModel.isLoading) LinearProgressIndicator()
        Button(onClick = {viewModel.register()}) {Text("Зарегистрироваться") }
        viewModel.errorMessage?.let { Text(it, color =  MaterialTheme.colorScheme.error) }

    }
}