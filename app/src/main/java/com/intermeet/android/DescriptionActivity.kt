package com.intermeet.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class DescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_descriptioin)
        // store user input of description
        val descriptionEdit : EditText = findViewById(R.id.IntroText)
        val description = descriptionEdit.text.toString()
        buttonFunc()
    }

    private
    fun buttonFunc(){
        val nextButton: Button = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, PromptsActivity::class.java)
            startActivity(intent)
        }
}
}