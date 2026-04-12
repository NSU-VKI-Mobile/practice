package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel

class Step2Activity : AppCompatActivity() {
    private lateinit var viewModel: DepositViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step2)

        viewModel = DepositViewModel(application)

        val initialAmount = intent.getDoubleExtra("INITIAL_AMOUNT", 0.0)
        val periodMonths = intent.getIntExtra("PERIOD_MONTHS", 0)

        viewModel.initialAmount = initialAmount
        viewModel.periodMonths = periodMonths

        val spinnerRate = findViewById<Spinner>(R.id.spinnerRate)
        val etMonthlyTopUp = findViewById<EditText>(R.id.etMonthlyTopUp)
        val btnBack = findViewById<Button>(R.id.btnBackStep2)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)

        val rate = viewModel.getInterestRateForPeriod()

        if (rate == null) {
            Toast.makeText(this, "Ошибка срока вклада", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val rates = listOf("$rate%")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRate.adapter = adapter
        viewModel.interestRate = rate

        btnBack.setOnClickListener { finish() }

        btnCalculate.setOnClickListener {
            val monthlyTopUp = etMonthlyTopUp.text.toString().toDoubleOrNull()
            viewModel.monthlyTopUp = monthlyTopUp

            viewModel.calculateInterest()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("INITIAL_AMOUNT", initialAmount)
            intent.putExtra("PERIOD_MONTHS", periodMonths)
            intent.putExtra("INTEREST_RATE", rate)
            intent.putExtra("MONTHLY_TOP_UP", monthlyTopUp ?: 0.0)
            intent.putExtra("FINAL_AMOUNT", viewModel.finalAmount)
            intent.putExtra("INTEREST_EARNED", viewModel.interestEarned)
            startActivity(intent)
        }
    }
}