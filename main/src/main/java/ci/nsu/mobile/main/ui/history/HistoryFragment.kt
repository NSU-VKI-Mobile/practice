package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        viewModel.init(requireContext())

        setupRecyclerView()
        observeData()

        binding.backButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_historyFragment_to_mainFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { calculation ->
            val dialog = DetailDialogFragment.newInstance(calculation)
            dialog.show(childFragmentManager, "DetailDialog")
        }

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.allCalculations.observe(viewLifecycleOwner) { calculations ->
            adapter.submitList(calculations)

            // Исправленная проверка
            if (calculations == null || calculations.isEmpty()) {
                binding.emptyTextView.visibility = View.VISIBLE
                binding.historyRecyclerView.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.GONE
                binding.historyRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}