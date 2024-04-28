package com.intermeet.android

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


import androidx.appcompat.widget.Toolbar
class HelpCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)
        // In your Activity or Fragment
        // In your Activity or Fragment
        // In your Activity or Fragment
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        val dropdownTab1 = findViewById<TextView>(R.id.dropdownTab1)
        val dropdownContent1 = findViewById<LinearLayout>(R.id.dropdownContent1)
        val dropdownTab2 = findViewById<TextView>(R.id.dropdownTab2)
        val dropdownContent2 = findViewById<LinearLayout>(R.id.dropdownContent2)
        val dropdownTab3 = findViewById<TextView>(R.id.dropdownTab3)
        val dropdownContent3 = findViewById<LinearLayout>(R.id.dropdownContent3)
        val dropdownTab4 = findViewById<TextView>(R.id.dropdownTab4)
        val dropdownContent4 = findViewById<LinearLayout>(R.id.dropdownContent4)
        val dropdownTab5 = findViewById<TextView>(R.id.dropdownTab5)
        val dropdownContent5 = findViewById<LinearLayout>(R.id.dropdownContent5)
        val dropdownTab6 = findViewById<TextView>(R.id.dropdownTab6)
        val dropdownContent6 = findViewById<LinearLayout>(R.id.dropdownContent6)
        val dropdownTab7 = findViewById<TextView>(R.id.dropdownTab7)
        val dropdownContent7 = findViewById<LinearLayout>(R.id.dropdownContent7)
        val dropdownTab8 = findViewById<TextView>(R.id.dropdownTab8)
        val dropdownContent8 = findViewById<LinearLayout>(R.id.dropdownContent8)
        val parentLayout = findViewById<LinearLayout>(R.id.parentLayout)
        val textView: TextView = findViewById(R.id.aboutUs)

        textView.post {
            val paint = textView.paint
            val width = textView.width.toFloat()
            val height = textView.textSize

            val positions = floatArrayOf(0.0f, 0.1f, 0.3f) // Start, mid, and end positions
            val colors = intArrayOf(
                Color.parseColor("#B56EFF"), // More saturated light purple
                Color.parseColor("#FF6A85"), // More saturated pink
                Color.parseColor("#FF8C00")  // More saturated peach (vivid orange)
            )

            val shader = LinearGradient(0f, 0f, width, height,
                colors, positions, Shader.TileMode.CLAMP)

            paint.shader = shader
            textView.invalidate()
        }
        val textView2: TextView = findViewById(R.id.myAccountSetting)

        textView2.post {
            val paint = textView2.paint
            val width = textView2.width.toFloat()
            val height = textView2.textSize

            val positions = floatArrayOf(0.0f, 0.3f, 0.5f) // Start, mid, and end positions
            val colors = intArrayOf(
                Color.parseColor("#B56EFF"), // More saturated light purple
                Color.parseColor("#FF6A85"), // More saturated pink
                Color.parseColor("#FF8C00")  // More saturated peach (vivid orange)
            )

            val shader = LinearGradient(0f, 0f, width, height,
                colors, positions, Shader.TileMode.CLAMP)

            paint.shader = shader
            textView2.invalidate()
        }
        val textView3: TextView = findViewById(R.id.publicProfile)

        textView3.post {
            val paint = textView3.paint
            val width = textView3.width.toFloat()
            val height = textView3.textSize

            val positions = floatArrayOf(0.0f, 0.3f, 0.5f) // Start, mid, and end positions
            val colors = intArrayOf(
                Color.parseColor("#B56EFF"), // More saturated light purple
                Color.parseColor("#FF6A85"), // More saturated pink
                Color.parseColor("#FF8C00")  // More saturated peach (vivid orange)
            )

            val shader = LinearGradient(0f, 0f, width, height,
                colors, positions, Shader.TileMode.CLAMP)

            paint.shader = shader
            textView3.invalidate()
        }


        dropdownTab1.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent1.visibility = if (dropdownContent1.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        dropdownTab2.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent2.visibility = if (dropdownContent2.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        dropdownTab3.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent3.visibility = if (dropdownContent3.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        dropdownTab4.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent4.visibility = if (dropdownContent4.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        dropdownTab5.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent5.visibility = if (dropdownContent5.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        dropdownTab6.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent6.visibility = if (dropdownContent6.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        dropdownTab7.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent7.visibility = if (dropdownContent7.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        dropdownTab8.setOnClickListener {
            TransitionManager.beginDelayedTransition(parentLayout)
            dropdownContent8.visibility = if (dropdownContent8.visibility == View.GONE) View.VISIBLE else View.GONE
        }
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                // Clear all activities on top of MainActivity and bring it to the top
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }




    }
}