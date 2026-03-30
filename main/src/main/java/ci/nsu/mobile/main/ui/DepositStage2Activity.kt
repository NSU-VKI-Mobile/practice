package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.util.IntentExtras
import ci.nsu.mobile.main.viewmodel.DepositStage2ViewModel

class DepositStage2Activity : AppCompatActivity() {

    private val viewModel: DepositStage2ViewModel by viewModels()

    private lateinit var editPeriodMonths: EditText
    private lateinit var spinnerInterestRate: Spinner
    private lateinit var textPeriodWarning: TextView
    private lateinit var editMonthlyTopUp: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_stage2)

        editPeriodMonths = findViewById(R.id.editPeriodMonths)
        spinnerInterestRate = findViewById(R.id.spinnerInterestRate)
        textPeriodWarning = findViewById(R.id.textPeriodWarning)
        editMonthlyTopUp = findViewById(R.id.editMonthlyTopUp)

        val buttonBackStage1: Button = findViewById(R.id.buttonBackStage1)
        val buttonCalculateResult: Button = findViewById(R.id.buttonCalculateResult)

        val initialAmount = intent.getDoubleExtra(IntentExtras.EXTRA_INITIAL_AMOUNT, Double.NaN)
        val periodMonths = intent.getIntExtra(IntentExtras.EXTRA_PERIOD_MONTHS, -1)
        if (!initialAmount.isNaN() && periodMonths > 0) {
            viewModel.setInitialAmountAndPeriodOnce(initialAmount, periodMonths)
            // Prefill inputs from Stage 1.
            if (editPeriodMonths.text.toString().isEmpty()) {
                editPeriodMonths.setText(periodMonths.toString())
            }
        }

        editPeriodMonths.doAfterTextChanged { text ->
            viewModel.onPeriodMonthsChanged(text?.toString().orEmpty())
        }
        editMonthlyTopUp.doAfterTextChanged { text ->
            viewModel.onMonthlyTopUpChanged(text?.toString().orEmpty())
        }

        // Spinner is driven by ViewModel options (usually one option derived from term).
        spinnerInterestRate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) = Unit
            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

        viewModel.uiState.observe(this) { uiState ->
            editPeriodMonths.error = uiState.periodMonthsError
            editMonthlyTopUp.error = uiState.monthlyTopUpError

            val shouldWarn = uiState.periodMonthsError == "Срок не указан"
            textPeriodWarning.visibility = if (shouldWarn) TextView.VISIBLE else TextView.GONE
            if (shouldWarn) {
                textPeriodWarning.text = uiState.periodMonthsError
            }

            val options = uiState.interestRateOptions
            val formattedOptions = options.map { "${it.toInt()}%" }
            val adapter = if (formattedOptions.isNotEmpty()) {
                ArrayAdapter(this, android.R.layout.simple_spinner_item, formattedOptions).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            } else {
                ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("—")).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }
            spinnerInterestRate.adapter = adapter
            spinnerInterestRate.isEnabled = uiState.selectedInterestRate != null

            // Single-option dropdown: keep selection aligned.
            uiState.selectedInterestRate?.let { rate ->
                val idx = options.indexOf(rate)
                if (idx >= 0) spinnerInterestRate.setSelection(idx, false)
            }
        }

        viewModel.navigationToResult.observe(this) { event ->
            val content = event.getContentIfNotHandled() ?: return@observe
            startActivity(
                Intent(this, DepositResultActivity::class.java).apply {
                    putExtra(IntentExtras.EXTRA_INITIAL_AMOUNT, content.initialAmount)
                    putExtra(IntentExtras.EXTRA_PERIOD_MONTHS, content.periodMonths)
                    putExtra(IntentExtras.EXTRA_INTEREST_RATE, content.interestRate)
                    putExtra(IntentExtras.EXTRA_FINAL_AMOUNT, content.finalAmount)
                    putExtra(IntentExtras.EXTRA_INTEREST_EARNED, content.interestEarned)

                    val hasTopUp = content.monthlyTopUp != null
                    putExtra(IntentExtras.EXTRA_HAS_MONTHLY_TOP_UP, hasTopUp)
                    if (hasTopUp) {
                        putExtra(IntentExtras.EXTRA_MONTHLY_TOP_UP, content.monthlyTopUp!!)
                    }
                }
            )
        }

        buttonBackStage1.setOnClickListener { finish() }
        buttonCalculateResult.setOnClickListener { viewModel.onCalculateClicked() }
    }
}

