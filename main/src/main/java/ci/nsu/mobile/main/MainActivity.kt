package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ci.nsu.mobile.main.di.ServiceLocator
import ci.nsu.mobile.main.ui.AuthApp
import ci.nsu.mobile.main.ui.AuthViewModel
import ci.nsu.mobile.main.ui.DepositViewModel

class MainActivity : ComponentActivity() {
    private val serviceLocator by lazy { ServiceLocator.get(applicationContext) }

    private val viewModel: AuthViewModel by viewModels {
        serviceLocator.viewModelFactory
    }

    private val depositViewModel: DepositViewModel by viewModels {
        serviceLocator.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AuthApp(
                viewModel = viewModel,
                depositViewModel = depositViewModel
            )
        }
    }
}
