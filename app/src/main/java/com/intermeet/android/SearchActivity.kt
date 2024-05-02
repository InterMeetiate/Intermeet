package com.intermeet.android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.EditText
import RecyclerViewAdapter
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView


class SearchActivity: AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchEventList: ListView
    private lateinit var adapter: RecyclerViewAdapter

    private val dataList: MutableList<Any> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat)

        searchEditText = findViewById(R.id.search_edit_text)
        searchEventList = findViewById(R.id.eventList)

        // Initialize RecyclerView adapter and layout manager
        //adapter = RecyclerViewAdapter()
        // Setup text watcher for search EditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                //performSearch(s.toString())
            }
        })
    }
    //private fun performSearch(query: String) {
        //val filteredList = dataList.filter()
        //adapter.submitList(filteredList)
    }
