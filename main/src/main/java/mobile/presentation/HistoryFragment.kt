package mobile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import kotlinx.coroutines.launch
import mobile.DepositApp

class HistoryFragment : Fragment() {

    private lateinit var viewModel: DepositViewModel
    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as DepositApp
        val factory = DepositViewModelFactory(app.repository)
        viewModel = ViewModelProvider(requireActivity(), factory)[DepositViewModel::class.java]

        val rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        // Настраиваем список
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapter

        // Подписываемся на поток данных из БД
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.historyFlow.collect { calculations ->
                // Как только база обновляется, мы закидываем новые данные в адаптер
                adapter.submitList(calculations)
            }
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}