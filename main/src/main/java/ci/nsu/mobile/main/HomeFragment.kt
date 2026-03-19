package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textViewTitle)
        textView.text = "Главный экран"


        val editText = view.findViewById<EditText>(R.id.editTextData)


        val buttonGoToSecond = view.findViewById<Button>(R.id.buttonGoToSecond)
        buttonGoToSecond.setOnClickListener {
            val intent = Intent(requireContext(), SecondActivity::class.java)

            val textToSend = editText.text.toString()
            intent.putExtra("EXTRA_DATA", if (textToSend.isEmpty()) "Пустое поле" else textToSend)
            startActivity(intent)
        }
    }
}