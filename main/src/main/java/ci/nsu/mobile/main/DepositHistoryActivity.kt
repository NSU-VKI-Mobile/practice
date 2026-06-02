package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.data.AppDatabase
import ci.nsu.mobile.main.data.DepositRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepositHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit_history)

        val tvEmptyHistory = findViewById<TextView>(R.id.tvEmptyHistory)
        val rvDepositHistory = findViewById<RecyclerView>(R.id.rvDepositHistory)
        val btnHistoryBack = findViewById<Button>(R.id.btnHistoryBack)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())
        val viewModel = DepositHistoryViewModel(repository)

        val adapter = DepositHistoryAdapter(emptyList()) { calculation ->
            val intent = Intent(this, DepositDetailsActivity::class.java)
            intent.putExtra(DepositDetailsActivity.EXTRA_CALCULATION_ID, calculation.id)
            startActivity(intent)
        }
        rvDepositHistory.adapter = adapter

        val btnClearHistory = Button(this)
        btnClearHistory.text = "Очистить историю"
        val root = findViewById<android.widget.LinearLayout>(android.R.id.content).getChildAt(0) as android.widget.LinearLayout
        root.addView(btnClearHistory, 1)

        btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show()
        }

        val swipeHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val calculation = adapter.getItem(viewHolder.adapterPosition)
                viewModel.deleteCalculation(calculation)
                Toast.makeText(this@DepositHistoryActivity, "Расчёт удалён", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHelper).attachToRecyclerView(rvDepositHistory)

        lifecycleScope.launch {
            viewModel.calculations.collectLatest { calculations ->
                adapter.updateList(calculations)

                if (calculations.isEmpty()) {
                    tvEmptyHistory.visibility = View.VISIBLE
                    rvDepositHistory.visibility = View.GONE
                } else {
                    tvEmptyHistory.visibility = View.GONE
                    rvDepositHistory.visibility = View.VISIBLE
                }
            }
        }

        btnHistoryBack.setOnClickListener {
            finish()
        }
    }
}
