package ci.nsu.mobile.main.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.databinding.ActivityMainBinding
import ci.nsu.mobile.main.ui.history.HistoryActivity
import ci.nsu.mobile.main.ui.step1.Step1Activity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnCalculate.setOnClickListener {
            val intent = Intent(this, Step1Activity::class.java)
            startActivity(intent)
        }

        binding.btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnClose.setOnClickListener {
            finishAffinity()
        }
    }
}