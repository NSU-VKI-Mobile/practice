package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCalculate: Button = findViewById(R.id.buttonCalculate)
        val buttonHistory: Button = findViewById(R.id.buttonHistory)
        val buttonClose: Button = findViewById(R.id.buttonClose)

        buttonCalculate.setOnClickListener {
            startActivity(Intent(this, DepositStage1Activity::class.java))
        }

        buttonHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        buttonClose.setOnClickListener {
            finishAffinity()
        }
    }
}

