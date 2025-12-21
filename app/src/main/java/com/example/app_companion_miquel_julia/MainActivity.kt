package com.example.app_companion_miquel_julia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var myNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myNavBar = findViewById(R.id.navbar)

        myNavBar.setOnItemSelectedListener { item -> handleNavigationItemSelected(item.itemId) }

        if (supportFragmentManager.findFragmentById(R.id.fragment) == null) {
            loadFragment(NewsFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
    }

    private fun handleNavigationItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.news -> {
                loadFragment(NewsFragment())
                true
            }

            R.id.agents -> {
                loadFragment(AgentsFragment())
                true
            }

            R.id.maps -> {
                loadFragment(MapsFragment())
                true
            }

            R.id.acts -> {
                loadFragment(ActsFragment())
                true
            }

            R.id.settings -> {
                loadFragment(SettingsFragment())
                true
            }

            else -> false
        }
    }
}