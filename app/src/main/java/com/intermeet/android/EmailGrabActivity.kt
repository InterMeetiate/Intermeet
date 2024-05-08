package com.intermeet.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class EmailGrabActivity : AppCompatActivity() {

    private lateinit var recoverPasswordButton: Button
    private lateinit var editEmail: EditText
    private lateinit var backButton: ImageView
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        recoverPasswordButton = findViewById(R.id.recover_password)
        editEmail = findViewById(R.id.edit_email)
        backButton = findViewById(R.id.back_arrow)
        mAuth = FirebaseAuth.getInstance()

        recoverPasswordButton.setOnClickListener {
            val userEmail = editEmail.text.toString()
            hideKeyboard(it)

            mAuth.sendPasswordResetEmail(userEmail)
                .addOnSuccessListener {
                    Toast.makeText(this, "Please check your email to reset your password", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "The email you provided was not found", Toast.LENGTH_SHORT).show()
                }
        }

        backButton.setOnClickListener {
            finish() // This will close the current activity and return to the previous one
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}