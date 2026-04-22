package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.vm.LoginAndRegViewModel

@Composable
fun MainScreen(
    onLogOutClick: () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
){
    viewModel.loadUsers()
    Box() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)) {
            items(viewModel.allUsers){item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(stringResource(R.string.id) + ": " + item.id)
                        Text(stringResource(R.string.text_login) + ": " + item.login)
                    }
                }
            }
        }
        Button(onClick = onLogOutClick) {
            Text(stringResource(R.string.text_logOut))
        }
    }
}