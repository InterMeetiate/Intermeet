package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast

class PromptsActivity : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var promptTextbox: EditText
    lateinit var enter: ImageView
    lateinit var promptList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prompts)

        listView = findViewById(R.id.listView)
        promptTextbox = findViewById(R.id.enter_prompt)
        enter = findViewById(R.id.add)

        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(this, android.R.layout.simple_list_item_1, promptList as List<String?>)

        listView.adapter = adapter

        enter.setOnClickListener {
            val text = promptTextbox.text.toString()
            if(text.isEmpty()) {
                Toast.makeText(this, "Please fill in the prompt.", Toast.LENGTH_SHORT).show()
            }
            else {
                promptList.add(text)
                adapter.notifyDataSetChanged()
            }
        }
    }
}