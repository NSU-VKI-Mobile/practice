package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DepositStepOneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_step_one)

        val etInitialAmount = findViewById<EditText>(R.id.etInitialAmount)
        val etPeriodMonths = findViewById<EditText>(R.id.etPeriodMonths)
        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnNext = findViewById<Button>(R.id.btnNext)

        btnHome.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            val amountText = etInitialAmount.text.toString()
            val periodText = etPeriodMonths.text.toString()

            if (amountText.isBlank() || periodText.isBlank()) {
                Toast.makeText(this, "Заполните стартовый взнос и срок вклада", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            val period = periodText.toIntOrNull()

            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Стартовый взнос должен быть больше 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (period == null || period <= 0) {
                Toast.makeText(this, "Срок вклада должен быть больше 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, DepositStepTwoActivity::class.java)
            intent.putExtra(EXTRA_INITIAL_AMOUNT, amount)
            intent.putExtra(EXTRA_PERIOD_MONTHS, period)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_INITIAL_AMOUNT = "initial_amount"
        const val EXTRA_PERIOD_MONTHS = "period_months"
    }
}
