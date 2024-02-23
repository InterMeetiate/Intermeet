package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.TextView
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
        val logo: TextView = findViewById(R.id.logo)
        val appName: TextView = findViewById(R.id.app_name)
        val whereWeIn: TextView = findViewById(R.id.where_we_in)
        val button2: Button = findViewById(R.id.button2)
        val signupbutton: Button = findViewById(R.id.sign_up_button)
        button2.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, LoginActivity::class.java)
            val logoAnimator = ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f)
            val appNameAnimator = ObjectAnimator.ofFloat(appName, "alpha", 1f, 0f)
            val whereWeInAnimator = ObjectAnimator.ofFloat(whereWeIn, "alpha", 1f, 0f)
            val signUpButtonAnimator = ObjectAnimator.ofFloat(signupbutton, "alpha", 1f, 0f)
            val button2Animator = ObjectAnimator.ofFloat(button2, "alpha", 1f, 0f)

            // Set duration for the animations (in milliseconds)
            val duration: Long = 500 // Customize this duration as needed

            // Apply an interpolator for the "ease out" effect
            val interpolator = AccelerateInterpolator()

            // Create an AnimatorSet to group the animations
            val animatorSet = AnimatorSet().apply {
                playTogether(logoAnimator, appNameAnimator, whereWeInAnimator, signUpButtonAnimator, button2Animator)
                this.duration = duration
                this.interpolator = interpolator
            }

            // Start the animations
            animatorSet.start()

            startActivity(intent)
        }
        signupbutton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }
    }
}