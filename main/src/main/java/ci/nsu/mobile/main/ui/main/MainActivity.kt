package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import ci.nsu.mobile.main.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        if (navHostFragment != null) {
            Log.d("MainActivity", "NavHostFragment найден, всё работает")
            // Здесь можно получить navController, но пока не нужно
        } else {
            Log.e("MainActivity", "NavHostFragment не найден! Проверьте activity_main.xml")
        }
    }
}