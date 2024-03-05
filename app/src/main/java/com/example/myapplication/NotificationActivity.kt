package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NotificationActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        ButtonFunc()
    }
    private
    fun ButtonFunc(){
        val agreeButton: Button = findViewById(R.id.Agree)
        agreeButton.setOnClickListener {
            //probably have logic here to handle notifications
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
        val signUpButton: Button = findViewById(R.id.notAllow)
        signUpButton.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)

        }
    }
}