package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentHistoryBinding
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DepositViewModel by activityViewModels {
        DepositViewModelFactory((requireActivity().application as ci.nsu.mobile.main.App).repository)
    }

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter { calculation ->
            val bundle = Bundle().apply {
                putLong("calculationId", calculation.id)
            }
            findNavController().navigate(R.id.action_historyFragment_to_detailFragment, bundle)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.allCalculations.observe(viewLifecycleOwner) { calculations ->
            adapter.submitList(calculations)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_mainFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}