package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.util.IntentExtras
import ci.nsu.mobile.main.ui.adapter.HistoryAdapter
import ci.nsu.mobile.main.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = HistoryAdapter { entity ->
            startActivity(Intent(this, DepositDetailActivity::class.java).apply {
                putExtra(IntentExtras.EXTRA_CALC_ID, entity.id)
            })
        }
        recyclerView.adapter = adapter

        viewModel.calculations.observe(this) { list ->
            adapter.submitList(list)
        }
    }
}

