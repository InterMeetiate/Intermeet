package com.intermeet.android

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PaymentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_payments)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val addCardBox: LinearLayout = findViewById(R.id.add_card_box)

        toolbar.setOnClickListener {
            // Handle back button click, navigate back to the previous activity
            finish()
        }

        addCardBox.setOnClickListener {
            // Handle add card box click, navigate to add card activity
            val intent = Intent(this, BirthdayActivity::class.java)
            startActivity(intent)
        }
    }
}
