package ci.nsu.mobile.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import ci.nsu.mobile.main.R

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Получаем строку переданную через Intent
        val receivedText = intent.getStringExtra("EXTRA_TEXT") ?: "Ничего не передано"

        val tvData = findViewById<TextView>(R.id.tvReceivedData)
        tvData.text = "Данные: $receivedText"

        // Кнопка назад в TopBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish() // Возвращаемся на MainActivity
        }
    }
}