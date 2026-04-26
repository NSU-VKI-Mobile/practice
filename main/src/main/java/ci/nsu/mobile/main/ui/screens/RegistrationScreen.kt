package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.navigation.Screens
import ci.nsu.mobile.main.ui.components.CustomButton

@Composable
fun RegistrationScreen(navTo: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextField("surname",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        TextField("name",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        TextField("lastName",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        //todo добавить иконку, по клику на которую открывается datePickerDialog
        TextField("birthday",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
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
        TextField("группа",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        TextField("login",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        TextField("password",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        TextField("email",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        TextField("phone",
            onValueChange = {},
            Modifier.padding(bottom = 10.dp))
        CustomButton({navTo(Screens.UsersScreen.route)}, "Зарегистрироваться")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReg() {
    RegistrationScreen({})
}