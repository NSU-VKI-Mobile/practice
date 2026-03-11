package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etInputText: EditText
    private lateinit var btnGoToSecond: Button
    private lateinit var btnGoToNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        etInputText = findViewById(R.id.etInputText)
        btnGoToSecond = findViewById(R.id.btnGoToSecond)
        btnGoToNav = findViewById(R.id.btnGoToNav)
    }

    private fun setupButtons() {
        btnGoToSecond.setOnClickListener {
            val inputText = etInputText.text.toString()

            if (inputText.isNotEmpty()) {
                // Передаем введенный текст на второй экран
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("key_data", inputText)
                startActivity(intent)
            } else {
                // Если текст не введен, показываем предупреждение
                Toast.makeText(this, "Введите текст!", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoToNav.setOnClickListener {
            // Переходим в NavActivity
            val intent = Intent(this, NavActivity::class.java)

            // Можем также передать текст и в NavActivity, если нужно
            val inputText = etInputText.text.toString()
            if (inputText.isNotEmpty()) {
                intent.putExtra("source", "MainActivity")
                intent.putExtra("message", inputText)
            }

            startActivity(intent)
        }
    }
}