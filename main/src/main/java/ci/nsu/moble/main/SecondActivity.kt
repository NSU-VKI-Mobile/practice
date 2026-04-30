package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Получаем сообщение из Intent
        val message = intent.getStringExtra("MESSAGE_KEY") ?: "Сообщение не получено"

        // Показываем сообщение
        val messageText = findViewById<TextView>(R.id.messageTextView)
        messageText.text = message

        // Кнопка назад в верхней панели
        val topBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topBar.setNavigationOnClickListener {
            finish()
        }
    }
}