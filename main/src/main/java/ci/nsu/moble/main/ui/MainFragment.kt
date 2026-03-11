package ci.nsu.moble.main.ui

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import ci.nsu.moble.main.R

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.button)
        val editText = view.findViewById<EditText>(R.id.editTextText)

        button.setOnClickListener {
            val text = editText.text.toString()
            val intent = Intent(requireContext(), MainActivity2::class.java)
            intent.putExtra("message", text)
            startActivity(intent)
        }
    }
}