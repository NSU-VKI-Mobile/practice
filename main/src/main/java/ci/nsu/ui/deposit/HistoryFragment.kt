package ci.nsu.ui.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    // Подтягиваем общую ViewModel через Koin
    private val viewModel: DepositViewModel by viewModel()
    private lateinit var adapter: CalculationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCalculations = view.findViewById<RecyclerView>(R.id.rvCalculations)
        val tvEmptyMessage = view.findViewById<TextView>(R.id.tvEmptyMessage)

        // Инициализируем адаптер и передаем туда действие удаления
        adapter = CalculationAdapter { calculation ->
            viewModel.deleteCalculation(calculation)
        }

        rvCalculations.layoutManager = LinearLayoutManager(requireContext())
        rvCalculations.adapter = adapter

        // Подписываемся на изменения в локальной БД Room через ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCalculations.collect { calculations ->
                    if (calculations.isEmpty()) {
                        tvEmptyMessage.visibility = View.VISIBLE
                        rvCalculations.visibility = View.GONE
                    } else {
                        tvEmptyMessage.visibility = View.GONE
                        rvCalculations.visibility = View.VISIBLE
                        adapter.submitList(calculations)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // При каждом открытии экрана истории обновляем данные для текущего пользователя
        viewModel.loadCalculations()
    }
}
