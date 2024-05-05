package com.intermeet.android

//import LikesPageFragment
import LikesPageFragment
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Places.initialize(applicationContext, "@string/google_maps_key")
        updateFCMToken()

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
                // set navigation for events fragment
                R.id.navigation_events -> {
                    val eventsFragment = EventsFragment.newInstance() // Assuming your EventsFragment also has a newInstance method
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, eventsFragment)
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
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d("FCM", "FCM Token: $token")
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
    private fun updateFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            val newToken = task.result
            Log.d("FCM", "FCM Token: $newToken")

            // Optionally send the token to your server
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let {
                val databaseReference = FirebaseDatabase.getInstance().getReference("users/$userId/fcmToken")
                databaseReference.setValue(newToken)
            }
        }
    }


}
//test