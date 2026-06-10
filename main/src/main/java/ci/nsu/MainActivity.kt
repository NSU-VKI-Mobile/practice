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

        // Находим наш NavHostFragment (контейнер для экранов)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Получаем контроллер навигации
        val navController = navHostFragment.navController

        // Находим нижнее меню в макете
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Связываем меню с контроллером навигации одной строчкой кода
        bottomNavigationView.setupWithNavController(navController)
    }
}
