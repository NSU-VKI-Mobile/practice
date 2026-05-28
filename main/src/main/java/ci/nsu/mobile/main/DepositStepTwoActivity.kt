package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DepositStepTwoActivity : AppCompatActivity() {

    private var initialAmount: Double = 0.0
    private var periodMonths: Int = 0
    private var interestRate: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_step_two)

        initialAmount = intent.getDoubleExtra(DepositStepOneActivity.EXTRA_INITIAL_AMOUNT, 0.0)
        periodMonths = intent.getIntExtra(DepositStepOneActivity.EXTRA_PERIOD_MONTHS, 0)
        interestRate = getRateByPeriod(periodMonths)

        val tvRateInfo = findViewById<TextView>(R.id.tvRateInfo)
        val etMonthlyTopUp = findViewById<EditText>(R.id.etMonthlyTopUp)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnCalculateResult = findViewById<Button>(R.id.btnCalculateResult)

        tvRateInfo.text = "Срок вклада: $periodMonths мес. Доступная ставка: ${interestRate.toInt()}%"

        btnBack.setOnClickListener {
            finish()
        }

        btnCalculateResult.setOnClickListener {
            val topUpText = etMonthlyTopUp.text.toString()
            val monthlyTopUp = if (topUpText.isBlank()) 0.0 else topUpText.toDoubleOrNull()

            if (monthlyTopUp == null || monthlyTopUp < 0) {
                Toast.makeText(this, "Пополнение не может быть отрицательным", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, DepositResultActivity::class.java)
            intent.putExtra(EXTRA_RESULT_INITIAL_AMOUNT, initialAmount)
            intent.putExtra(EXTRA_RESULT_PERIOD_MONTHS, periodMonths)
            intent.putExtra(EXTRA_RESULT_INTEREST_RATE, interestRate)
            intent.putExtra(EXTRA_RESULT_MONTHLY_TOP_UP, monthlyTopUp)
            startActivity(intent)
        }
    }

    private fun getRateByPeriod(period: Int): Double {
        return when {
            period < 6 -> 15.0
            period < 12 -> 10.0
            else -> 5.0
        }
    }

    companion object {
        const val EXTRA_RESULT_INITIAL_AMOUNT = "result_initial_amount"
        const val EXTRA_RESULT_PERIOD_MONTHS = "result_period_months"
        const val EXTRA_RESULT_INTEREST_RATE = "result_interest_rate"
        const val EXTRA_RESULT_MONTHLY_TOP_UP = "result_monthly_top_up"
    }
}
