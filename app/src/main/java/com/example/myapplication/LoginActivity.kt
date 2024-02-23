package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        easeInTransition()
    }
    private fun easeInTransition() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.ease_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        // Apply fade in animation to the logo
        findViewById<TextView>(R.id.Logo).startAnimation(fadeIn)

        // Apply slide up animation to the rest of the elements
        findViewById<EditText>(R.id.signupUsername).startAnimation(slideUp)
        findViewById<EditText>(R.id.signupPassword).startAnimation(slideUp)
        findViewById<Button>(R.id.loginButton).startAnimation(slideUp)
        findViewById<Button>(R.id.forgotPassword).startAnimation(slideUp)
        findViewById<Button>(R.id.loginText).startAnimation(slideUp)

    }
}