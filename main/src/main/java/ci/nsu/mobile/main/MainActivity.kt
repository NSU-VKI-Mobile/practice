package ci.nsu.mobile.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostContainer) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Привязываем нижнее меню к навигации через sealed class
        bottomNav.setOnItemSelectedListener { item ->
            val screen = when (item.itemId) {
                R.id.homeFragment -> BottomNavScreen.Home
                R.id.profileFragment -> BottomNavScreen.Profile
                R.id.settingsFragment -> BottomNavScreen.Settings
                else -> return@setOnItemSelectedListener false
            }
            navController.navigate(
                when (screen) {
                    is BottomNavScreen.Home -> R.id.homeFragment
                    is BottomNavScreen.Profile -> R.id.profileFragment
                    is BottomNavScreen.Settings -> R.id.settingsFragment
                }
            )
            true
        }
    }
}