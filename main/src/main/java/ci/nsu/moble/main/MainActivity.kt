// Task_3: Главная Activity с BottomNavigationView и NavHostFragment.
// NavController управляет переходами между экранами нижнего меню.
// Список возможных экранов описан sealed class NavDestination.

package ci.nsu.moble.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ci.nsu.moble.main.databinding.ActivityMainBinding
import ci.nsu.moble.main.nvgs.NavDestination

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        // Sealed class используется для описания возможных состояний навигации
        val currentDestination = NavDestination.Home
        supportActionBar?.title = currentDestination.title
    }
}
