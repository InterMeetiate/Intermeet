package com.intermeet.android.SignUp_SignIn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.intermeet.android.R

class NotificationActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        ButtonFunc()
    }
    private
    fun ButtonFunc(){
        val agreeButton: Button = findViewById(R.id.allow)
        agreeButton.setOnClickListener {
            //probably have logic here to handle notifications
            val intent = Intent(this, PhotoUploadActivity::class.java)
            startActivity(intent)
        }
        val signUpButton: Button = findViewById(R.id.dont_allow)
        signUpButton.setOnClickListener{
            val intent = Intent(this, PhotoUploadActivity::class.java)
            startActivity(intent)

        }
    }
}