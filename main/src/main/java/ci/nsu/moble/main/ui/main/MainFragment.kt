package ci.nsu.moble.main.ui.main

import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.LinearLayout

import ci.nsu.moble.main.R

private val colorsMap = mapOf(
    "red" to Color.RED,
    "orange" to Color.parseColor("#FFA500"),
    "yellow" to Color.YELLOW,
    "green" to Color.GREEN,
    "blue" to Color.BLUE,
    "indigo" to Color.parseColor("#4B0082"),
    "violet" to Color.parseColor("#8000FF")
)

class MainFragment : Fragment() {

    //      задал переменные editTextInput и buttonCheck
//    private lateinit var editTextInput: EditText
//    private lateinit var buttonCheck: Button
    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
//      задал переменные editTextInput и buttonCheck
        val editTextInput = view.findViewById<EditText>(R.id.editText)
        val buttonCheck = view.findViewById<Button>(R.id.button)
        val pallete = view.findViewById<LinearLayout>(R.id.palleteContainer)
        pallete.removeAllViews()

        buttonCheck.setOnClickListener {
            val text = editTextInput.text.toString().lowercase()
            val color = colorsMap[text]
            if (color != null)
                buttonCheck.setBackgroundColor(color)
            else
                Log.d("MainFragment", "Неизвестный цвет: $text")
//            if (text == "red")
//                buttonCheck.setBackgroundColor(Color.RED)
//            else if (text == "orange")
//                buttonCheck.setBackgroundColor(Color.parseColor("#FFA500"))
//            else if (text == "yellow")
//                buttonCheck.setBackgroundColor(Color.YELLOW)
//            else if (text == "green")
//                buttonCheck.setBackgroundColor(Color.GREEN)
//            else if (text == "blue")
//                buttonCheck.setBackgroundColor(Color.BLUE)
//            else if (text == "indigo")
//                buttonCheck.setBackgroundColor(Color.parseColor("#4B0082"))
//            else if (text == "violet")
//                buttonCheck.setBackgroundColor(Color.parseColor("#8000FF"))
//            else
//                Log.d("MainFragment", "Неизвестный цвет: $text")
//            val color = when (text) {
//                "red" -> Color.RED
//                "orange" -> Color.parseColor("#FFA500")
//                "yellow" -> Color.YELLOW
//                "green" -> Color.GREEN
//                "blue" -> Color.BLUE
//                "indigo" -> Color.parseColor("#4B0082")
//                "violet" -> Color.parseColor("#8000FF")
//                else -> Color.parseColor("#CACCD2")
//            }
//            buttonCheck.setBackgroundColor(color)
        }

        colorsMap.forEach { (colorName, color) ->
            val colorButton = Button(requireContext())
            val colorNameUp = colorName.replaceFirstChar { it.uppercase() }
            colorButton.text = colorNameUp
            colorButton.isAllCaps = false
            colorButton.setBackgroundColor(color)
            colorButton.setTextColor(Color.WHITE)
            colorButton.setOnClickListener {
                editTextInput.setText(colorNameUp)
            }

            pallete.addView(colorButton)
        }

        return view
    }

}