package ci.nsu.moble.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import ci.nsu.moble.main.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        bottomNav.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commit()
                    true
                }

                R.id.window2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment2.newInstance())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }
}