package com.intermeet.android

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class ProfileEmailActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var checkmark: ImageView
    private lateinit var verifyButton: Button
    private lateinit var toolbar: Toolbar
    private lateinit var auth: FirebaseAuth
    private lateinit var userRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_email)
        val userId = "lFAyIZN8XoQhVMJdKCgmIQF1yEk2"
        val database = Firebase.database

        emailInput = findViewById(R.id.email_input)
        checkmark = findViewById(R.id.checkmark)
        verifyButton = findViewById(R.id.verify_email)
        toolbar = findViewById(R.id.toolbar)

        auth = FirebaseAuth.getInstance()

        // Initialize userRef with the provided userId
        userRef = database.getReference("users").child(userId).child("email")

        // Set click listener for the verify button
        verifyButton.setOnClickListener {
            val email = emailInput.text.toString()
            verifyEmail(email)
        }

        // Set click listener for the back button
        toolbar.setOnClickListener {
            // Navigate back to the settings page
            // Replace `com.intermeet.android.SettingsActivity` with the appropriate activity class name
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it when pressing back
        }
    }

    private fun verifyEmail(email: String) {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot : DataSnapshot) {
                val user = dataSnapshot.getValue<String>()
                Log.d(TAG, "User email: $user")
                Log.d(TAG,"check${user == email}")
                if (user != null) {
                    if (user == email) {
                        // Email is verified, show the checkmark and set tint color to blue
                        checkmark.visibility = View.VISIBLE
                        checkmark.setColorFilter(Color.BLUE)
                    } else {
                        // Email is not verified, hide the checkmark
                        checkmark.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e(TAG, "Error reading user data", error.toException())
            }
        })
    }

    companion object {
        private const val TAG = "ProfileEmailActivity"
    }
}