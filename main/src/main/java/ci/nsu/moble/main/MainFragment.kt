package ci.nsu.moble.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_step1Fragment)
        }
        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }
        binding.btnClose.setOnClickListener { requireActivity().finish() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
