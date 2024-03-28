package com.intermeet.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_discover -> {
                    val discoverFragment = DiscoverFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, discoverFragment)
                        .commit()
                    true
                }
                // Handle other navigation items if you have more
                else -> false
            }
        }

        // Set default selection (optional, if you want to show a particular tab on launch)
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.navigation_discover
        }
    }
}
//test