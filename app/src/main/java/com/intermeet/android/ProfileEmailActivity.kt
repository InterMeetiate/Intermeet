package com.intermeet.android


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.EditText
import com.intermeet.android.R

class ProfileEmailActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var checkmark: ImageView
    private lateinit var verifyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_email)

        emailInput = findViewById(R.id.email_input)
        checkmark = findViewById(R.id.checkmark)
        verifyButton = findViewById(R.id.verify_email)

        verifyButton.setOnClickListener {
            val email = emailInput.text.toString()
            if (isEmailVerified(email)) {
                // Email is verified, show the checkmark and set tint color to blue
                checkmark.visibility = View.VISIBLE
                checkmark.setColorFilter(Color.BLUE)
            } else {
                // Email is not verified, hide the checkmark
                checkmark.visibility = View.GONE
            }
        }
    }

    // Function to check if email is verified
    private fun isEmailVerified(email: String): Boolean {
        // Implement your email verification logic here
        // For demonstration purposes, I'm returning true always
        return true
    }
}
