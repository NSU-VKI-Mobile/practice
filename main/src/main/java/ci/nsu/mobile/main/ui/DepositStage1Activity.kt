package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.util.IntentExtras
import ci.nsu.mobile.main.viewmodel.DepositStage1ViewModel

class DepositStage1Activity : AppCompatActivity() {

    private val viewModel: DepositStage1ViewModel by viewModels()

    private lateinit var editInitialAmount: EditText
    private lateinit var editPeriodMonths: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_stage1)

        editInitialAmount = findViewById(R.id.editInitialAmount)
        editPeriodMonths = findViewById(R.id.editPeriodMonths)

        val buttonBackHome: Button = findViewById(R.id.buttonBackHome)
        val buttonNext: Button = findViewById(R.id.buttonNext)

        editInitialAmount.doAfterTextChanged { text ->
            viewModel.onInitialAmountChanged(text?.toString().orEmpty())
        }
        editPeriodMonths.doAfterTextChanged { text ->
            viewModel.onPeriodMonthsChanged(text?.toString().orEmpty())
        }

        viewModel.uiState.observe(this) { uiState ->
            if (editInitialAmount.text.toString() != uiState.initialAmountText) {
                // Avoid cursor jump: we rely on text watchers above for updates.
            }
            editInitialAmount.error = uiState.initialAmountError
            editPeriodMonths.error = uiState.periodMonthsError
        }

        viewModel.navigationToStage2.observe(this) { event ->
            val content = event.getContentIfNotHandled() ?: return@observe
            startActivity(
                Intent(this, DepositStage2Activity::class.java).apply {
                    putExtra(IntentExtras.EXTRA_INITIAL_AMOUNT, content.initialAmount)
                    putExtra(IntentExtras.EXTRA_PERIOD_MONTHS, content.periodMonths)
                }
            )
        }

        buttonBackHome.setOnClickListener {
            finish()
        }

        buttonNext.setOnClickListener {
            viewModel.onNextClicked()
        }
    }
}

