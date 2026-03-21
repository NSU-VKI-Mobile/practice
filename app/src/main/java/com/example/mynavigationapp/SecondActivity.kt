package com.example.mynavigationapp
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SecondActivity : AppCompatActivity() {


    private lateinit var toolbar: Toolbar
    private lateinit var textViewReceivedData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        toolbar = findViewById(R.id.toolbar)
        textViewReceivedData = findViewById(R.id.textViewReceivedData)

        // Настраиваем Toolbar как верхнюю панель
        setSupportActionBar(toolbar)

        // Показываем стрелку "Назад" на панели
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Устанавливаем заголовок
        supportActionBar?.title = "Второй экран"
        val receivedText = intent.getStringExtra("USER_DATA") ?: "Данные не получены"

        textViewReceivedData.text = "Вы ввели: $receivedText"
    }

    //  метод нажатие на стрелку "Назад"
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}