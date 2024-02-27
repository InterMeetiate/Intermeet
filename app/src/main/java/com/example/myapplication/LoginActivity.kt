package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        buttonFunc()
    }

    private
    fun buttonFunc(){
        val signInButton: Button = findViewById(R.id.loginText)
        signInButton.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
}
}