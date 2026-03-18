package ci.nsu.moble.main.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.graphics.Color
import android.os.Debug
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import ci.nsu.moble.main.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    private val colorsMap = mapOf(
        "red" to Color.RED,
        "yellow" to Color.YELLOW,
        "green" to Color.GREEN,
        "blue" to Color.BLUE,
        "magenta" to Color.MAGENTA,
        "cyan" to Color.CYAN
    )

    private val addedColors = mutableListOf<String>().apply {
        addAll(colorsMap.keys)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.editTextText)
        val button = view.findViewById<Button>(R.id.button)
        val listView = view.findViewById<ListView>(R.id.list1)

        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            addedColors
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                val colorName = addedColors[position]
                textView.text = colorName.replaceFirstChar { it.uppercase() }


                colorsMap[colorName.lowercase()]?.let { colorInt ->
                    textView.setBackgroundColor(colorInt)


                    val brightness = (Color.red(colorInt) * 0.299 +
                            Color.green(colorInt) * 0.587 +
                            Color.blue(colorInt) * 0.114)
                    textView.setTextColor(if (brightness < 128) Color.WHITE else Color.BLACK)
                }
                return view
            }
        }

        listView.adapter = adapter

        button.setOnClickListener {
            val input = editText.text.toString().trim().lowercase()

            val color = colorsMap[input]

            if (color != null) {

                button.setBackgroundColor(color)
            } else {
                Log.d("Ошибка", "Цвет не найден")
            }
        }
    }
}
