package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ci.nsu.mobile.main.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private lateinit var lvHistory: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val historyItems = mutableListOf<String>()
    private var depositIds = mutableListOf<Long>()

    private val depositViewModel: DepositViewModel by activityViewModels()

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lvHistory = view.findViewById(R.id.lv_history)

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            historyItems
        )
        lvHistory.adapter = adapter

        // Observe history changes
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                depositViewModel.history.collect { deposits ->
                    updateHistoryList(deposits)
                }
            }
        }

        // Load history
        depositViewModel.loadHistory()

        lvHistory.setOnItemClickListener { _, _, position, _ ->
            if (position < depositIds.size) {
                val depositId = depositIds[position]
                // Navigate to detail fragment
                val detailFragment = DepositDetailFragment.newInstance(depositId)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun updateHistoryList(deposits: List<ci.nsu.mobile.main.data.db.DepositEntity>) {
        historyItems.clear()
        depositIds.clear()

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        for (deposit in deposits) {
            val date = dateFormat.format(Date(deposit.calculationDate))
            val item = "$date | Старт: ${deposit.initialAmount} | Итог: ${deposit.finalAmount}"
            historyItems.add(item)
            depositIds.add(deposit.id)
        }

        adapter.notifyDataSetChanged()
    }
}