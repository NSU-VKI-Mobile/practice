package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()
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

        setupRecyclerView()
        observeData()


        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            Toast.makeText(requireContext(), "История очищена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { calculation ->
            val bundle = Bundle()
            bundle.putSerializable("calculation", calculation)
            findNavController().navigate(R.id.action_historyFragment_to_detailDialogFragment, bundle)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeData() {
        viewModel.calculations.observe(viewLifecycleOwner) { calculations ->
            if (calculations != null) {
                adapter.submitList(calculations)

                if (calculations.isEmpty()) {
                    binding.tvEmptyHistory.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.tvEmptyHistory.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}