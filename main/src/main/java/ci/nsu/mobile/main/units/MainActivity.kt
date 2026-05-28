package ci.nsu.mobile.main.units

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import  ci.nsu.mobile.main.units.ui.navigation.NavGraph
import ci.nsu.mobile.main.units.ui.theme.RestAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestAppTheme {
                NavGraph()
            }
        }
    }
}