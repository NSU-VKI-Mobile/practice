package ci.nsu.mobile.main.presentation.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
class Step1Fragment : Fragment(R.layout.fragment_step1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val a = view.findViewById<EditText>(R.id.amount)
        val m = view.findViewById<EditText>(R.id.months)

        view.findViewById<Button>(R.id.next).setOnClickListener {

            val bundle = bundleOf(
                "a" to a.text.toString().toDoubleOrNull(),
                "m" to m.text.toString().toIntOrNull()
            )

            // 🔥 ИЗМЕНЕНО: используем R.id
            findNavController().navigate(R.id.step2, bundle)
        }
    }
}
