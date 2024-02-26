package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class BeginningActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beginning)

        val constraintLayout: ConstraintLayout = findViewById(R.id.beginningLayout)

        val animationDrawable: AnimationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
        buttonFunc()
    }
    private
    fun buttonFunc(){
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val signupbutton: Button = findViewById(R.id.sign_up_button)
        signupbutton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}