package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val format = NumberFormat.getNumberInstance(Locale("ru", "RU"))

        val initialAmount = intent.getDoubleExtra("INITIAL_AMOUNT", 0.0)
        val periodMonths = intent.getIntExtra("PERIOD_MONTHS", 0)
        val interestRate = intent.getDoubleExtra("INTEREST_RATE", 0.0)
        val monthlyTopUp = intent.getDoubleExtra("MONTHLY_TOP_UP", 0.0)
        val finalAmount = intent.getDoubleExtra("FINAL_AMOUNT", 0.0)
        val interestEarned = intent.getDoubleExtra("INTEREST_EARNED", 0.0)

        findViewById<TextView>(R.id.tvInitialAmount).text = "Стартовый взнос: ${format.format(initialAmount)} ₽"
        findViewById<TextView>(R.id.tvPeriod).text = "Срок: $periodMonths мес."
        findViewById<TextView>(R.id.tvRate).text = "Ставка: $interestRate%"

        findViewById<TextView>(R.id.tvMonthlyTopUp).text = if (monthlyTopUp > 0) {
            "Ежемесячное пополнение: ${format.format(monthlyTopUp)} ₽"
        } else {
            "Ежемесячное пополнение: нет"
        }

        findViewById<TextView>(R.id.tvFinalAmount).text = "Итоговая сумма: ${format.format(finalAmount)} ₽"
        findViewById<TextView>(R.id.tvInterestEarned).text = "Начисленные проценты: ${format.format(interestEarned)} ₽"

        val database = AppDatabase.getDatabase(application)
        val repository = DepositRepository(database)

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val calculation = DepositCalculation(
                    initialAmount = initialAmount,
                    periodMonths = periodMonths,
                    interestRate = interestRate,
                    monthlyTopUp = if (monthlyTopUp > 0) monthlyTopUp else null,
                    finalAmount = finalAmount,
                    interestEarned = interestEarned,
                    calculationDate = System.currentTimeMillis()
                )
                repository.insertDeposit(calculation)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ResultActivity, "Расчёт сохранён", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.btnToMain).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}