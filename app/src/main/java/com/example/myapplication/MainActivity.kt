package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Переход к расчету (Этап 1)
        binding.btnCalculate.setOnClickListener {
            startActivity(Intent(this, Step1Activity::class.java))
        }

        // Переход к истории
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // Закрытие приложения
        binding.btnClose.setOnClickListener {
            finishAffinity() // Закрывает все Activity в стеке
        }
    }
}