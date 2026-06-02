package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
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
        periodMonths = intent.getIntExtra(DepositStepOneActivity.EXTRA_PERIOD_MONTHS, 1)
        interestRate = getRateByPeriod(periodMonths)

        val tvRateInfo = findViewById<TextView>(R.id.tvRateInfo)
        val etMonthlyTopUp = findViewById<EditText>(R.id.etMonthlyTopUp)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnCalculateResult = findViewById<Button>(R.id.btnCalculateResult)

        val root = (findViewById<ViewGroup>(android.R.id.content).getChildAt(0) as LinearLayout)

        val tvMonthTitle = TextView(this)
        tvMonthTitle.text = "Срок вклада"
        tvMonthTitle.textSize = 16f

        val spinnerPeriodMonths = Spinner(this)
        val months = (1..24).toList()
        val monthTexts = months.map { month -> "$month мес." }
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthTexts)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPeriodMonths.adapter = monthAdapter

        val tvRateTitle = TextView(this)
        tvRateTitle.text = "Процентная ставка"
        tvRateTitle.textSize = 16f

        val spinnerInterestRate = Spinner(this)
        val rates = listOf(15, 10, 5)
        val rateTexts = rates.map { rate -> "$rate%" }
        val rateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rateTexts)
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInterestRate.adapter = rateAdapter

        root.addView(tvMonthTitle, 2)
        root.addView(spinnerPeriodMonths, 3)
        root.addView(tvRateTitle, 4)
        root.addView(spinnerInterestRate, 5)

        var isUpdating = false

        spinnerPeriodMonths.setSelection(months.indexOf(periodMonths))
        spinnerInterestRate.setSelection(rates.indexOf(interestRate.toInt()))
        updateRateInfo(tvRateInfo)

        spinnerPeriodMonths.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (isUpdating) return

                periodMonths = months[position]
                interestRate = getRateByPeriod(periodMonths)

                isUpdating = true
                spinnerInterestRate.setSelection(rates.indexOf(interestRate.toInt()))
                isUpdating = false

                updateRateInfo(tvRateInfo)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerInterestRate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (isUpdating) return

                interestRate = rates[position].toDouble()
                periodMonths = getPeriodByRate(interestRate)

                isUpdating = true
                spinnerPeriodMonths.setSelection(months.indexOf(periodMonths))
                isUpdating = false

                updateRateInfo(tvRateInfo)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
            period <= 6 -> 15.0
            period <= 11 -> 10.0
            else -> 5.0
        }
    }

    private fun getPeriodByRate(rate: Double): Int {
        return when (rate.toInt()) {
            15 -> 6
            10 -> 7
            else -> 12
        }
    }

    private fun updateRateInfo(tvRateInfo: TextView) {
        tvRateInfo.text = "Срок вклада: $periodMonths мес. Ставка: ${interestRate.toInt()}%"
    }

    companion object {
        const val EXTRA_RESULT_INITIAL_AMOUNT = "result_initial_amount"
        const val EXTRA_RESULT_PERIOD_MONTHS = "result_period_months"
        const val EXTRA_RESULT_INTEREST_RATE = "result_interest_rate"
        const val EXTRA_RESULT_MONTHLY_TOP_UP = "result_monthly_top_up"
    }
}
