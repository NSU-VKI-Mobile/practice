package ci.nsu.mobile.main.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.databinding.ActivityHistoryBinding
import ci.nsu.mobile.main.model.DepositCalculation
import ci.nsu.mobile.main.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupUI()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { calculation ->
            openHistoryDetail(calculation)
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = this@HistoryActivity.adapter
        }
    }

    private fun setupUI() {
        binding.btnBackToStart.setOnClickListener {
            goToMainScreen()
        }

        binding.btnClearHistory.setOnClickListener {
            showClearConfirmationDialog()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadCalculations()
        }
    }

    private fun showClearConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Очистить историю")
            .setMessage("Вы уверены, что хотите удалить все расчёты? Это действие нельзя отменить.")
            .setPositiveButton("Очистить") { _, _ ->
                viewModel.clearAllHistory()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.calculations.observe(this) { calculations ->
            if (calculations.isNullOrEmpty()) {
                showEmptyState(true)
                adapter.submitList(emptyList())
            } else {
                showEmptyState(false)
                adapter.submitList(calculations)
            }
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                binding.tvError.text = it
                binding.tvError.visibility = android.view.View.VISIBLE
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.clearSuccess.observe(this) { success ->
            if (success) {
                Snackbar.make(
                    binding.root,
                    "История успешно очищена",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showEmptyState(show: Boolean) {
        if (show) {
            binding.rvHistory.visibility = android.view.View.GONE
            binding.tvEmpty.visibility = android.view.View.VISIBLE
            binding.tvError.visibility = android.view.View.GONE
        } else {
            binding.rvHistory.visibility = android.view.View.VISIBLE
            binding.tvEmpty.visibility = android.view.View.GONE
        }
    }

    private fun openHistoryDetail(calculation: DepositCalculation) {
        val intent = Intent(this, HistoryDetailActivity::class.java)
        intent.putExtra("calculation_id", calculation.id)
        startActivity(intent)
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}