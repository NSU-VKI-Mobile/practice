package com.example.mynavigationapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Создаём view из layout-файла
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Находим кнопку
        val btnGoToSecond = view.findViewById<Button>(R.id.btnGoToSecondActivity)

        // Обрабатываем нажатие
        btnGoToSecond.setOnClickListener {
            // Создаём Intent для перехода на SecondActivity
            val intent = Intent(requireContext(), SecondActivity::class.java)
            intent.putExtra("USER_DATA", "HomeFragment")
            startActivity(intent)
        }

        return view
    }
}