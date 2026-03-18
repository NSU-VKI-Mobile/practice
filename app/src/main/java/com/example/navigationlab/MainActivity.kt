package com.example.navigationlab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etData: EditText
    private lateinit var btnGoToSecond: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etData = findViewById(R.id.etData)
        btnGoToSecond = findViewById(R.id.btnGoToSecond)

        btnGoToSecond.setOnClickListener {
            val textToSend = etData.text.toString()

            // Создаем Intent для перехода на SecondActivity
            val intent = Intent(this, SecondActivity::class.java)
            // Передаем данные
            intent.putExtra("EXTRA_DATA", textToSend)
            startActivity(intent)
        }
    }
}