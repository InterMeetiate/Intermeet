package com.intermeet.android

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.intermeet.android.SignUp_SignIn.LoginActivity
import com.intermeet.android.SignUp_SignIn.SignupActivity

//hihihi
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
        val signInButton: Button = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val signUpButton: Button = findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }
    }
}