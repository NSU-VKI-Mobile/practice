// Task_1: Fragment — UI слой. Подключён ViewModel.
// Добавлена кнопка для переключения на Compose Activity.

package ci.nsu.moble.main.ui.main

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import ci.nsu.moble.main.MainActivity3
import ci.nsu.moble.main.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goToComposeBtn = view.findViewById<Button>(R.id.go_to_compose_btn)
        goToComposeBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity3::class.java))
        }
    }

}