package com.example.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.R
import com.example.auth.vm.LoginAndRegViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistryScreen(
    onRegClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var expandedSex by remember { mutableStateOf(false) }
    var expandedGroup by remember { mutableStateOf(false) }
    Column() {
        OutlinedTextField(
            value =  uiState.firstName,
            onValueChange = { newText -> viewModel.setFirstName(newText) },
            label = { Text(text = stringResource(R.string.text_name)) }
        )
        OutlinedTextField(
            value = uiState.lastName,
            onValueChange = { newText -> viewModel.setLastName(newText) },
            label = { Text(stringResource(R.string.text_lastName)) }
        )
        OutlinedTextField(
            value =  uiState.middleName,
            onValueChange = { newText -> viewModel.setMiddleName(newText) },
            label = { Text(text = stringResource(R.string.text_middleName)) }
        )
        OutlinedTextField(
            value =  uiState.birthDate ?: "",
            onValueChange = { newText -> viewModel.setBirthDate(newText) },
            label = { Text(text = stringResource(R.string.text_birthDay)) },
            isError = !uiState.isBirthDateValid
        )
        ExposedDropdownMenuBox(
            expanded = expandedSex,
            onExpandedChange = { expandedSex = !expandedSex }
        ) {
            OutlinedTextField(
                value = uiState.gender ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.text_sex)) } ,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSex) },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                isError = !uiState.isGenderValid
            )

            ExposedDropdownMenu(
                expanded = expandedSex,
                onDismissRequest = { expandedSex = false },
            ) {
                viewModel.allGenders.forEach { gen ->
                    DropdownMenuItem(
                        text = { Text(gen) },
                        onClick = {viewModel.setGender(gen)}
                    )
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = expandedGroup,
            onExpandedChange = { expandedGroup = !expandedGroup }
        ) {
            OutlinedTextField(
                value = uiState.group?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.text_group)) } ,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                isError = !uiState.isGenderValid
            )

            ExposedDropdownMenu(
                expanded = expandedGroup,
                onDismissRequest = { expandedGroup = false },
            ) {
                viewModel.allGroup.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.name) },
                        onClick = {viewModel.setGroup(group)}
                    )
                }
            }
        }
        OutlinedTextField(
            value =  uiState.regLogin,
            onValueChange = { newText -> viewModel.setRegLogin(newText) },
            label = { Text(text = stringResource(R.string.text_login)) }
        )
        OutlinedTextField(
            value =  uiState.regPassword,
            onValueChange = { newText -> viewModel.setRegPassword(newText) },
            label = { Text(text = stringResource(R.string.text_password)) }
        )
        OutlinedTextField(
            value =  uiState.email,
            onValueChange = { newText -> viewModel.setEmail(newText) },
            label = { Text(text = stringResource(R.string.text_email)) }
        )
        OutlinedTextField(
            value =  uiState.phone,
            onValueChange = { newText -> viewModel.setPhone(newText) },
            label = { Text(text = stringResource(R.string.text_phone)) }
        )
        Button(onClick = onRegClick, enabled = uiState.isAllCorrect) {
            Text(stringResource(R.string.text_registry))
        }
        Button(onClick = onBackClick) {
            Text(stringResource(R.string.text_back))
        }
    }
}