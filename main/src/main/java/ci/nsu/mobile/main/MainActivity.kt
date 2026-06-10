package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import ci.nsu.mobile.main.data.local.TokenManager
import ci.nsu.mobile.main.ui.screens.*
import ci.nsu.mobile.main.ui.navigation.MainNavGraph
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)



        setContent {
            MainNavGraph()


        }
    }
}