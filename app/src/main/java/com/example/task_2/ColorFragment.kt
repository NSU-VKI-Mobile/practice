package com.example.task_2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ColorFragment : Fragment() {

    private val colorMap = mapOf(
        "red" to Color.RED,
        "green" to Color.GREEN,
        "blue" to Color.BLUE,
        "yellow" to Color.YELLOW,
        "orange" to Color.parseColor("#FFA500"),
        "purple" to Color.parseColor("#FF800080"),
        "pink" to Color.parseColor("#FFFFC0CB"),
        "brown" to Color.parseColor("#FFA52A2A"),
        "gray" to Color.GRAY,
        "cyan" to Color.CYAN,
        "magenta" to Color.MAGENTA,
        "lime" to Color.parseColor("#FF32CD32"),
        "navy" to Color.parseColor("#FF000080"),
        "teal" to Color.parseColor("#FF008080"),
        "coral" to Color.parseColor("#FFFF7F50"),
        "gold" to Color.parseColor("#FFFFD700"),
        "silver" to Color.parseColor("#FFC0C0C0"),
        "beige" to Color.parseColor("#FFF5F5DC"),
        "black" to Color.BLACK,
        "white" to Color.WHITE
    )

    private lateinit var editColorName: EditText
    private lateinit var btnSearchColor: Button
    private lateinit var rvColorPalette: RecyclerView
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_color, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editColorName = view.findViewById(R.id.editColorName)
        btnSearchColor = view.findViewById(R.id.btnSearchColor)
        rvColorPalette = view.findViewById(R.id.rvColorPalette)

        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val colorList = colorMap.map { ColorItem(it.key, it.value) }
        colorAdapter = ColorAdapter(colorList)
        rvColorPalette.layoutManager = LinearLayoutManager(requireContext())
        rvColorPalette.adapter = colorAdapter
    }

    private fun setupClickListeners() {
        btnSearchColor.setOnClickListener {
            val colorName = editColorName.text.toString().trim().lowercase()
            
            if (colorName.isEmpty()) {
                return@setOnClickListener
            }

            val color = colorMap[colorName]
            if (color != null) {
                btnSearchColor.setBackgroundColor(color)
            } else {
                Log.i("ColorFragment", "Пользовательский цвет \"$colorName\" не найден")
            }
        }
    }
}

data class ColorItem(val name: String, val color: Int)

class ColorAdapter(private val colors: List<ColorItem>) : 
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorView: View = view.findViewById(R.id.colorView)
        val tvColorName: TextView = view.findViewById(R.id.tvColorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val item = colors[position]
        holder.colorView.setBackgroundColor(item.color)
        holder.tvColorName.text = item.name.replaceFirstChar { it.uppercase() }
    }

    override fun getItemCount(): Int = colors.size
}
