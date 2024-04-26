package com.intermeet.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class TipCenter : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_center)
        val toolbar: Toolbar = findViewById(R.id.toolbar)





        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                // Clear all activities on top of MainActivity and bring it to the top
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
    }
}