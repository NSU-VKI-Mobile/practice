package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calculateButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_firstStepFragment)
        }

        binding.historyButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_historyFragment)
        }

        binding.closeButton.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}