package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        val receivedData = intent.getStringExtra("EXTRA_DATA") ?: "Нет данных"


        val textView = findViewById<TextView>(R.id.textViewData)
        textView.text = receivedData


        val topBar = findViewById<MaterialToolbar>(R.id.topAppBar)


        topBar.setNavigationOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}