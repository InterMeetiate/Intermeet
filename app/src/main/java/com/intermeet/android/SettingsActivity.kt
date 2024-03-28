package com.intermeet.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val emailButton: Button = findViewById(R.id.Email)
        val paymentsButton: Button = findViewById(R.id.Payments)
        val phoneNumberButton: Button = findViewById(R.id.PhoneNumber)

        // Set click listener for the toolbar navigation icon (back button)
        toolbar.setNavigationOnClickListener {
            // Navigate back to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
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
    }
}
