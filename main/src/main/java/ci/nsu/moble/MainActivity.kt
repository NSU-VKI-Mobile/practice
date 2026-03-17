package ci.nsu.moble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ci.nsu.mobile.main.R
import ci.nsu.moble.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}