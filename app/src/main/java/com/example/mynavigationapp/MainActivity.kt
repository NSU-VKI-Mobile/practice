package com.example.mynavigationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // Переменные для элементов интерфейса
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var editTextData: EditText
    private lateinit var btnGoToSecond: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // элементы на экране
        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        editTextData = findViewById(R.id.editTextData)
        btnGoToSecond = findViewById(R.id.btnGoToSecond)

        // Настраиваем Toolbar как верхнюю панель
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Главная"

        // НАСТРАИВАЕМ НАВИГАЦИЮ
        setupNavigation()

        // Обработка кнопки перехода на SecondActivity
        btnGoToSecond.setOnClickListener {
            val userText = editTextData.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("USER_DATA", userText)
            startActivity(intent)
        }
    }

    private fun setupNavigation() {
        // Получаем NavController из NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // СВЯЗЫВАЕМ нижнее меню с навигацией

        bottomNavigationView.setupWithNavController(navController)

        //  сменой фрагментов
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> supportActionBar?.title = "Главная"
                R.id.profileFragment -> supportActionBar?.title = "Профиль"
                R.id.settingsFragment -> supportActionBar?.title = "Настройки"
            }
        }
    }
}