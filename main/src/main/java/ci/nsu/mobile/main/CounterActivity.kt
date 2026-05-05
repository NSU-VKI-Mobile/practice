package ci.nsu.mobile.main

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CounterActivity : AppCompatActivity() {

    private val viewModel: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        val tvCount = findViewById<TextView>(R.id.tvCount)
        val btnIncrement = findViewById<Button>(R.id.btnIncrement)
        val btnDecrement = findViewById<Button>(R.id.btnDecrement)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)

        val adapter = HistoryAdapter(emptyList())
        rvHistory.adapter = adapter

        btnIncrement.setOnClickListener { viewModel.increment() }
        btnDecrement.setOnClickListener { viewModel.decrement() }
        btnReset.setOnClickListener { viewModel.reset() }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                tvCount.text = state.count.toString()
                adapter.updateList(state.history)
            }
        }
    }
}