package com.intermeet.android

import android.content.Intent
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
                R.id.navigation_account -> {
                    val profileFragment = ProfileFragment.newInstance() // Assuming your ProfileFragment also has a newInstance method
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, profileFragment)
                        .commit()
                    true
                }
                R.id.navigation_worth -> {
                    val likesFragment = LikesPageFragment.newInstance() // Assuming your ProfileFragment also has a newInstance method
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, likesFragment)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == SettingsActivity.RESULT_GO_TO_PROFILE) {
            val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.profileFragment // Assume this is the ID for the profile in the BottomNavigationView
        }
    }
}
//test