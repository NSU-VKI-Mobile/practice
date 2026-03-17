package com.example.practice

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var viewModel: MyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        val button = view.findViewById<Button>(R.id.my_button)
        button.setOnClickListener {
            viewModel.counter++
            button.text = "Нажато: ${viewModel.counter}"
        }
    }
}