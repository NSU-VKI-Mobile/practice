// Task_3: Home-экран. Содержит кнопку перехода на SecondActivity с передачей строки.

package ci.nsu.moble.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ci.nsu.moble.main.R
import ci.nsu.moble.main.SecondActivity
import ci.nsu.moble.main.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goToSecondBtn.setOnClickListener {
            val intent = Intent(requireContext(), SecondActivity::class.java).apply {
                putExtra(SecondActivity.EXTRA_DATA, "Hello from Home!")
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
