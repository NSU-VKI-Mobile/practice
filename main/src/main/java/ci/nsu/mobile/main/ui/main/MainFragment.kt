package ci.nsu.mobile.main.ui.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import ci.nsu.mobile.main.R

class MainFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var listView: ListView

    private val colors = listOf("Red", "Green", "Blue", "Yellow")

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        editText = view.findViewById(R.id.editText)
        button = view.findViewById(R.id.button)
        listView = view.findViewById(R.id.colorList)

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            colors
        )
        listView.adapter = adapter

        button.setOnClickListener {
            val text = editText.text.toString()
            Log.d("TASK2", "Button pressed. Text = $text")

            when (text.lowercase()) {
                "red" -> {
                    button.setBackgroundColor(Color.RED)
                    Log.d("TASK2", "Color changed to red")
                }
                "green" -> {
                    button.setBackgroundColor(Color.GREEN)
                    Log.d("TASK2", "Color changed to green")
                }
                "blue" -> {
                    button.setBackgroundColor(Color.BLUE)
                    Log.d("TASK2", "Color changed to blue")
                }
                "yellow" -> {
                    button.setBackgroundColor(Color.YELLOW)
                    Log.d("TASK2", "Color changed to yellow")
                }
                else -> Log.d("TASK2", "Color not found")
            }
        }

        return view
    }

}
