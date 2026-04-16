package ci.nsu.mobile.main.presentation.view
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<Button>(R.id.calc).setOnClickListener {
            findNavController().navigate(R.id.step1)
        }

        view.findViewById<Button>(R.id.history).setOnClickListener {
            findNavController().navigate(R.id.history)
        }

        view.findViewById<Button>(R.id.exit).setOnClickListener {
            requireActivity().finish()
        }
    }
}

