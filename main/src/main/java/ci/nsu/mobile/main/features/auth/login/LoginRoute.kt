package ci.nsu.mobile.main.features.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ci.nsu.mobile.main.core.base.AppEvent


@Composable
fun LoginRoute(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AppEvent.Navigate -> { navController.navigate(event.route) }
                else -> {}
            }

        }
    }

    val state by viewModel.state.collectAsState()
    LoginScreen(
        state = state,
        onButtonClick = viewModel::login,
        onLinkClick = viewModel::toRegister,
        onFieldChange = viewModel::onFieldChange,
    )
}