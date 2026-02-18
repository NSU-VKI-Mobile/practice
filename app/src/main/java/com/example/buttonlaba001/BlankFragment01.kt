package com.example.buttonlaba001

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class BlankFragment01 : Fragment() {

    private lateinit var button: Button
    private var currentColorIndex = 0
    private val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфляция макета фрагмента
        val view = inflater.inflate(R.layout.fragment_blank01, container, false)

        button = view.findViewById(R.id.my_button)
        button.setBackgroundColor(colors[currentColorIndex])

        // Установка слушателя нажатий на кнопку
        button.setOnClickListener {
            currentColorIndex = (currentColorIndex + 1) % colors.size
            button.setBackgroundColor(colors[currentColorIndex])
        }

        return view
    }
}
