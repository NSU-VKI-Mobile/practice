package ci.nsu.mobile.main.features.auth.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import ci.nsu.mobile.main.core.base.AppEvent


@Composable
fun RegisterRoute(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
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
    RegisterScreen(
        state = state,
        onButtonClick = viewModel::register,
        onLinkClick = viewModel::toLogin,
        onFieldChange = viewModel::onFieldChange,
    )
}