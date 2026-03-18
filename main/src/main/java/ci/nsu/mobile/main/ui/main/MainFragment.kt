package ci.nsu.mobile.main.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.graphics.Color
import android.util.Log
import ci.nsu.mobile.main.R

enum class AppColor(val colorName: String, val hexCode: String, val textColor: Int = Color.BLACK) {
    RED("red", "#FF0000"),
    ORANGE("orange", "#FF8000"),
    YELLOW("yellow", "#FFEA00"),
    GREEN("green", "#04FF00"),
    BLUE("blue", "#002FFF", Color.WHITE),
    INDIGO("indigo", "#46307A", Color.WHITE),
    VIOLET("violet", "#8400FF", Color.WHITE);

    companion object {
        fun findByName(input: String): AppColor? {
            val cleaned = input.trim().lowercase()
            return values().find { it.colorName == cleaned }
        }
    }
}

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val TAG = "MainFragment"
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var editTextColor: EditText
    private lateinit var buttonApply: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        editTextColor = view.findViewById(R.id.editTextText)
        buttonApply = view.findViewById(R.id.button2)

        buttonApply.setOnClickListener {
            changeButtonColor()
        }

        return view
    }

    private fun changeButtonColor() {
        val colorName = editTextColor.text.toString().trim().lowercase()

        Log.d(TAG, "Пользователь ввел: $colorName")

        if (colorName.isEmpty()) {
            Log.w(TAG, "Пустой ввод")
            return
        }

        val color = AppColor.findByName(colorName)

        if (color != null) {
            try {
                buttonApply.setBackgroundColor(Color.parseColor(color.hexCode))
                buttonApply.setTextColor(color.textColor)
                Log.i(TAG, "Цвет изменен на ${color.colorName}")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка: ${e.message}")
            }
        } else {
            Log.w(TAG, "Цвет '$colorName' не найден")
        }
    }
}