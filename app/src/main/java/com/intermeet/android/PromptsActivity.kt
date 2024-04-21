package com.intermeet.android

import CustomAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import com.intermeet.android.helperFunc.getUserDataRepository

class PromptsActivity : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var promptTextbox: EditText
    lateinit var enter: ImageView
    lateinit var promptDropdown: Spinner
    lateinit var promptList: ArrayList<String>
    private lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prompts)
        backButton = findViewById(R.id.next_button)


        listView = findViewById(R.id.listView)
        promptTextbox = findViewById(R.id.enter_prompt)
        enter = findViewById(R.id.add)
        promptDropdown = findViewById(R.id.prompt_spinner)

        promptList = ArrayList()

        val userDataRepository = getUserDataRepository()
        promptList.addAll(userDataRepository.userData?.prompts ?: listOf())

        val adapter = CustomAdapter(this, promptList, listView)
        listView.adapter = adapter

        enter.setOnClickListener {
            // Get the prompt selection and the user entered text
            val text = promptTextbox.text.toString()
            val selectedPrompt = promptDropdown.selectedItem.toString()
            val combinedText = "$selectedPrompt\n$text"

            if (text.isEmpty()) {
                Toast.makeText(this, "Please fill in the prompt.", Toast.LENGTH_SHORT).show()
            } else {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(promptTextbox.windowToken, 0)

                // Adds text to list and userDataRepository
                promptList.add(combinedText)
                userDataRepository.userData?.prompts?.add(combinedText)
                adapter.notifyDataSetChanged()

                // Clear textbox after adding it to the list
                promptTextbox.setText("")
            }
        }
        backButton.setOnClickListener {
            // Intent to navigate to the SecondActivity
            Log.d("PhotoActivity", "Current userData: ${userDataRepository.userData}")
            val intent = Intent(this, AccountCreationActivity::class.java)
            startActivity(intent)
        }
    }
}

