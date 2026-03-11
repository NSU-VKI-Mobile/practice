package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import ci.nsu.mobile.main.fragments.*
import ci.nsu.mobile.main.navigation.NavigationState

class NavActivity : AppCompatActivity() {

    private var currentState: NavigationState = NavigationState.Home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)

        // Получаем данные из Intent
        val source = intent.getStringExtra("source")
        val message = intent.getStringExtra("message")

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        // Отображаем полученные данные в Toolbar
        if (source != null && message != null) {
            toolbar.title = "Из $source"
            toolbar.subtitle = message

            // Показываем Toast с полученными данными
            Toast.makeText(this, "Получено от $source: $message", Toast.LENGTH_LONG).show()

            // Если есть сообщение, можем передать его в детальный фрагмент
            if (source == "MainActivity" || source == "SecondActivity") {
                // Сохраняем сообщение для передачи в DetailsFragment
                val bundle = Bundle().apply {
                    putString("data_key", message)
                }
                // Здесь мы можем сохранить bundle для использования позже
            }
        } else {
            setupToolbar(toolbar)
        }

        setupBottomNavigation()

        // Загружаем начальный фрагмент
        if (savedInstanceState == null) {
            navigateTo(NavigationState.Home)
        }
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navigateTo(NavigationState.Home)
                    true
                }
                R.id.nav_profile -> {
                    navigateTo(NavigationState.Profile)
                    true
                }
                R.id.nav_settings -> {
                    navigateTo(NavigationState.Settings)
                    true
                }
                R.id.nav_details -> {
                    val bundle = Bundle().apply {
                        // Если есть сообщение из Intent, передаем его в детали
                        val message = intent.getStringExtra("message")
                        if (message != null) {
                            putString("data_key", message)
                        } else {
                            putString("data_key", "Данные из Bottom Navigation")
                        }
                    }
                    navigateTo(NavigationState.Details, bundle)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateTo(state: NavigationState, bundle: Bundle? = null) {
        currentState = state

        val fragment: Fragment = when (state) {
            is NavigationState.Home -> HomeFragment()
            is NavigationState.Profile -> ProfileFragment()
            is NavigationState.Settings -> SettingsFragment()
            is NavigationState.Details -> DetailsFragment()
        }

        bundle?.let {
            fragment.arguments = it
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}