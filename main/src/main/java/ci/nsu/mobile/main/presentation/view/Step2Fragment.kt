package ci.nsu.mobile.main.presentation.view
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
class Step2Fragment : Fragment(R.layout.fragment_step2) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val m = requireArguments().getInt("m")
        val a = requireArguments().getDouble("a")

        val rate = when {
            m < 6 -> 15.0
            m < 12 -> 10.0
            else -> 5.0
        }

        val top = view.findViewById<EditText>(R.id.top)

        view.findViewById<Button>(R.id.calc).setOnClickListener {

            val bundle = bundleOf(
                "a" to a,
                "m" to m,
                "r" to rate,
                "t" to top.text.toString().toDoubleOrNull()
            )


            findNavController().navigate(R.id.result, bundle)
        }
    }
}
