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


    private val colorMap = mapOf(
        "Red" to Color.RED,
        "Green" to Color.GREEN,
        "Blue" to Color.BLUE,
        "Yellow" to Color.YELLOW
    )

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
            colorMap.keys.toList()
        )
        listView.adapter = adapter

        button.setOnClickListener {
            val text = editText.text.toString().lowercase()
            Log.d("TASK2", "Button pressed. Text = $text")

            val color = colorMap[text]
            if (color != null) {
                button.setBackgroundColor(color)
                Log.d("TASK2", "Color changed to $text")
            } else {
                Log.d("TASK2", "Color not found")
            }
        }

        return view
    }
}