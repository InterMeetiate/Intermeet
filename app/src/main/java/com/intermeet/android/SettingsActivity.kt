package com.intermeet.android

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    companion object {
        const val RESULT_GO_TO_PROFILE = 1 // A unique code to identify the specific result.
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val emailButton: Button = findViewById(R.id.Email)
        val paymentsButton: Button = findViewById(R.id.Payments)
        val phoneNumberButton: Button = findViewById(R.id.PhoneNumber)
        val logout: Button = findViewById(R.id.LogOut)

        // Set click listener for the toolbar navigation icon (back button)
        toolbar.setNavigationOnClickListener {
            // Navigate back to MainActivity and try to show ProfileFragment
            val intent = Intent(this, MainActivity::class.java).apply {
                // Clear all activities on top of MainActivity and bring it to the top
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }

        // Set click listener for the email button
        emailButton.setOnClickListener {
            // Navigate to the ProfileEmailActivity
            val intent = Intent(this, ProfileEmailActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for the payments button
        paymentsButton.setOnClickListener {
            // Navigate to the PaymentsActivity
            val intent = Intent(this, PaymentsActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for the phone number button
        phoneNumberButton.setOnClickListener {
            // Navigate to the PhoneNumberActivity
            val intent = Intent(this, PhoneNumberActivity::class.java)
            startActivity(intent)
        }

        logout.setOnClickListener {
            // Show a loading indicator or a simple toast message
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, BeginningActivity::class.java).apply {
                // Clear the activity stack.
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            val options = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
            startActivity(intent, options.toBundle())
        }

    }
}
