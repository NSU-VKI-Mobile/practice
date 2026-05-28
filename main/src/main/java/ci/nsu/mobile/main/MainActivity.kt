package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val btnHistory = findViewById<Button>(R.id.btnHistory)
        val btnClose = findViewById<Button>(R.id.btnClose)

        btnCalculate.setOnClickListener {
            val intent = Intent(this, DepositStepOneActivity::class.java)
            startActivity(intent)
        }

        btnHistory.setOnClickListener {
            Toast.makeText(this, "Историю добавим в следующей части", Toast.LENGTH_SHORT).show()
        }

        btnClose.setOnClickListener {
            finishAffinity()
        }
    }
}
