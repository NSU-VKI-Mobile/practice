package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.database.AppDatabase
import ci.nsu.mobile.main.data.database.DepositCalculation
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: HistoryListAdapter
    private val decimalFormat = DecimalFormat("#,##0.00")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        tvEmpty = view.findViewById(R.id.tvEmptyHistory)

        setupRecyclerView()
        loadHistory()

        view.findViewById<android.widget.Button>(R.id.btnClearHistory)?.setOnClickListener {
            clearHistory()
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryListAdapter(decimalFormat, dateFormat)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                val calculations = db.depositDao().getAllCalculations()

                if (calculations.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    tvEmpty.visibility = View.GONE
                    adapter.submitList(calculations)
                }
            } catch (e: Exception) {
                recyclerView.visibility = View.GONE
                tvEmpty.visibility = View.VISIBLE
                tvEmpty.text = "Ошибка загрузки: ${e.message}"
            }
        }
    }

    private fun clearHistory() {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(requireContext())
                db.depositDao().deleteAll()
                loadHistory()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class HistoryListAdapter(
        private val decimalFormat: DecimalFormat,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

        private var items: List<DepositCalculation> = emptyList()

        fun submitList(list: List<DepositCalculation>) {
            items = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history, parent, false)
            return ViewHolder(view, decimalFormat, dateFormat)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        class ViewHolder(
            itemView: View,
            private val decimalFormat: DecimalFormat,
            private val dateFormat: SimpleDateFormat
        ) : RecyclerView.ViewHolder(itemView) {

            private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
            private val tvInitialAmount: TextView = itemView.findViewById(R.id.tvInitialAmount)
            private val tvFinalAmount: TextView = itemView.findViewById(R.id.tvFinalAmount)

            fun bind(calculation: DepositCalculation) {
                tvDate.text = dateFormat.format(Date(calculation.calculationDate))
                tvInitialAmount.text = "${decimalFormat.format(calculation.initialAmount)} ₽"
                tvFinalAmount.text = "${decimalFormat.format(calculation.finalAmount)} ₽"
            }
        }
    }
}