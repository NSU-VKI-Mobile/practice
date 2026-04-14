package ci.nsu.mobile.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import ci.nsu.mobile.main.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Важно: используем правильный ID
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            (it as androidx.navigation.fragment.NavHostFragment).navController
        }

        if (navController != null) {
            val appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment))
            setupActionBarWithNavController(navController, appBarConfiguration)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            (it as androidx.navigation.fragment.NavHostFragment).navController
        }
        return navController?.navigateUp() == true || super.onSupportNavigateUp()
    }
}