package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.viewmodel.MainViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var lvHistory: ListView

    private val depositViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            DepositRepository(
                DepositDatabase.getDatabase(requireContext()).depositDao()
            )
        )
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lvHistory = view.findViewById(R.id.lv_history)

        val btnBack = view.findViewById<Button>(R.id.btn_back)

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            depositViewModel.history.collectLatest { historyList ->
                val items = historyList.map {
                    "Старт: ${it.initialAmount} | Срок: ${it.periodMonths} мес | Итог: ${it.finalAmount}"
                }

                lvHistory.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    items
                )
            }
        }

        lvHistory.setOnItemClickListener { _, _, position, _ ->
            val selected = depositViewModel.history.value[position]

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ResultFragment.newInstance(
                    selected.initialAmount,
                    selected.periodMonths,
                    selected.interestRate,
                    selected.monthlyTopUp,
                    selected.finalAmount,
                    selected.interestEarned,
                    true
                ))
                .addToBackStack(null)
                .commit()
        }
    }
}