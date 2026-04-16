package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.databinding.HistoryFragmentBinding

class HistoryFragment : Fragment() {

    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = DepositRepository(
            ci.nsu.mobile.main.data.database.AppDatabase.getDatabase(requireContext()).depositDao()
        )
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HistoryViewModel(repository) as T
            }
        })[HistoryViewModel::class.java]

        adapter = HistoryAdapter { calculation ->
            val bundle = Bundle().apply {
                putLong("calculationId", calculation.id)
            }
            findNavController().navigate(R.id.action_historyFragment_to_detailFragment, bundle)
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        viewModel.calculations.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
            binding.tvEmptyHistory.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}