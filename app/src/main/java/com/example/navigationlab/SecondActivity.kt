package com.example.navigationlab

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var tvReceivedData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        tvReceivedData = findViewById(R.id.tvReceivedData)

        //  переданные данные
        val receivedText = intent.getStringExtra("EXTRA_DATA")

        if (!receivedText.isNullOrEmpty()) {
            tvReceivedData.text = "Получено: $receivedText"
        } else {
            tvReceivedData.text = "Данные не получены"
        }
    }
}