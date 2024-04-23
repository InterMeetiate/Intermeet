package com.intermeet.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import com.intermeet.android.R
import com.intermeet.android.SettingsActivity

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
