package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Получаем переданные данные
        val receivedData = intent.getStringExtra("key_data") ?: "Данные не получены"
        val tvReceivedData = findViewById<TextView>(R.id.tvReceivedData)
        tvReceivedData.text = "Получено: $receivedData"

        setupToolbar()
        setupButtons()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Кнопка "Назад" в TopBar возвращает в MainActivity
        toolbar.setNavigationOnClickListener {
            finish() // Просто закрываем текущую Activity и возвращаемся к предыдущей
        }
    }

    private fun setupButtons() {
        val btnBackToMain = findViewById<Button>(R.id.btnBackToMain)
        val btnGoToNav = findViewById<Button>(R.id.btnGoToNav)

        btnBackToMain.setOnClickListener {
            // Простой способ вернуться в MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Закрываем текущую Activity
        }

        btnGoToNav.setOnClickListener {
            val intent = Intent(this, NavActivity::class.java)
            startActivity(intent)
        }
    }
}