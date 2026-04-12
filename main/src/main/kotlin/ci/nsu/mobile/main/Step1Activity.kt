package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Step1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step1)

        val etInitialAmount = findViewById<EditText>(R.id.etInitialAmount)
        val etPeriodMonths = findViewById<EditText>(R.id.etPeriodMonths)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)

        btnBack.setOnClickListener { finish() }

        btnNext.setOnClickListener {
            val initialAmount = etInitialAmount.text.toString().toDoubleOrNull()
            val periodMonths = etPeriodMonths.text.toString().toIntOrNull()

            if (initialAmount == null || initialAmount <= 0) {
                Toast.makeText(this, "Введите корректный стартовый взнос", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (periodMonths == null || periodMonths <= 0) {
                Toast.makeText(this, "Введите корректный срок вклада", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, Step2Activity::class.java)
            intent.putExtra("INITIAL_AMOUNT", initialAmount)
            intent.putExtra("PERIOD_MONTHS", periodMonths)
            startActivity(intent)
        }
    }
}