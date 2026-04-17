package ci.nsu.mobile.main.ui.screens

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
import ci.nsu.mobile.main.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistryScreen(
    onRegClick: () -> Unit
){
    var expandedSex by remember { mutableStateOf(false) }
    var expandedGroup by remember { mutableStateOf(false) }
    val listOfSex = listOf('М','Ж')
    val listOfSpacer = listOf("space1","space2","space3")
    val spacer = ""
    val boolSpacer = true
    Column() {
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_name)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value = spacer,
            onValueChange = { newText -> spacer },
            label = { Text(stringResource(R.string.text_lastName)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_middleName)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_birthDay)) },
            isError = boolSpacer
        )
        ExposedDropdownMenuBox(
            expanded = expandedSex,
            onExpandedChange = { expandedSex = !expandedSex }
        ) {
            OutlinedTextField(
                value = spacer,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.text_sex)) } ,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSex) },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                isError = boolSpacer
            )

            ExposedDropdownMenu(
                expanded = expandedSex,
                onDismissRequest = { expandedSex = false },
            ) {
                listOfSex.forEach { sex ->
                    DropdownMenuItem(
                        text = { Text(sex.toString()) },
                        onClick = {}
                    )
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = expandedGroup,
            onExpandedChange = { expandedGroup = !expandedGroup }
        ) {
            OutlinedTextField(
                value = spacer,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.text_group)) } ,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGroup) },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                isError = boolSpacer
            )

            ExposedDropdownMenu(
                expanded = expandedGroup,
                onDismissRequest = { expandedGroup = false },
            ) {
                listOfSpacer.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group.toString()) },
                        onClick = {}
                    )
                }
            }
        }
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_login)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_password)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_email)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_phone)) },
            isError = boolSpacer
        )
        Button(onClick = onRegClick) {
            Text(stringResource(R.string.text_registry))
        }
    }
}