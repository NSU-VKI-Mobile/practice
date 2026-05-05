package ci.nsu.mobile.main.features.main

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import ci.nsu.mobile.main.core.base.AppEvent

@Composable
fun MainRoute(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AppEvent.Navigate -> { navController.navigate(event.route) }
                is AppEvent.Exit -> { (context as? Activity)?.finishAffinity() }
            }
        }
    }

    val state by viewModel.state.collectAsState()
    MainScreen(
        state = state,
        onRefreshButtonClick = viewModel::refresh,
        onExitButtonClick = viewModel::exit,
        onLogoutButtonClick = viewModel::logout
    )
}