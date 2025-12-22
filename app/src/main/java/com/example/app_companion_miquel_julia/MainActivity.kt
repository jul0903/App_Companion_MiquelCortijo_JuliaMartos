package com.example.app_companion_miquel_julia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics

private lateinit var analytics: FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var myNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        analytics = FirebaseAnalytics.getInstance(this)

        myNavBar = findViewById(R.id.navbar)

        myNavBar.setOnItemSelectedListener { item -> handleNavigationItemSelected(item.itemId) }

        if (supportFragmentManager.findFragmentById(R.id.fragment) == null) {
            loadFragment(AgentsFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
    }

    private fun handleNavigationItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.agents -> {
                analytics.logEvent("agentsPage", null)
                loadFragment(AgentsFragment())
                true
            }

            R.id.maps -> {
                analytics.logEvent("mapsPage", null)
                loadFragment(MapsFragment())
                true
            }

            R.id.acts -> {
                analytics.logEvent("actsPage", null)
                loadFragment(ActsFragment())
                true
            }

            R.id.settings -> {
                analytics.logEvent("settingsPage", null)
                loadFragment(SettingsFragment())
                true
            }
            else -> false
        }
    }
}