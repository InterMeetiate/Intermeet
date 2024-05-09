package com.intermeet.android

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginProgressBar: ProgressBar
    private lateinit var loginButton: TextView
    private lateinit var forgotPasswordButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.signInEmail)
        val passwordEditText: EditText = findViewById(R.id.signInPassword)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        forgotPasswordButton = findViewById(R.id.forgotPassword)
        loginButton = findViewById(R.id.loginButton)
        loginProgressBar = findViewById(R.id.loginProgressBar)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            logIn(email, password)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordButton.setOnClickListener {
            val intent = Intent(this, EmailGrabActivity::class.java)
            startActivity(intent)
        }

        // Add animations to buttons
        setupButtonAnimations(loginButton)
        setupButtonAnimations(signUpButton)
        setupButtonAnimations(forgotPasswordButton)

        emailEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                passwordEditText.requestFocus()
                true
            } else {
                false
            }
        }

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()
                logIn(email, password)
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun setupButtonAnimations(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(200).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                }
            }
            false // Return false to allow the click event to proceed
        }
    }

    private fun logIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            showProgressBar()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    hideProgressBar()
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        } else {
            hideProgressBar()
            Toast.makeText(this, "Invalid Email and Password.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(baseContext, "Please sign in to continue.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showProgressBar() {
        loginProgressBar.visibility = View.VISIBLE
        loginButton.visibility = View.GONE
        forgotPasswordButton.visibility = View.GONE
    }

    private fun hideProgressBar() {
        loginProgressBar.visibility = View.GONE
        loginButton.visibility = View.VISIBLE
        forgotPasswordButton.visibility = View.VISIBLE
    }
}
