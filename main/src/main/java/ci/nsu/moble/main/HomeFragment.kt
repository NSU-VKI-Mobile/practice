package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val messageInput = view.findViewById<EditText>(R.id.messageInput)
        val button = view.findViewById<Button>(R.id.goToSecondActivityButton)

        button.setOnClickListener {

            val userMessage = messageInput.text.toString()


            val intent = Intent(requireActivity(), SecondActivity::class.java)
            intent.putExtra("MESSAGE_KEY", userMessage)
            startActivity(intent)
        }

        return view
    }
}