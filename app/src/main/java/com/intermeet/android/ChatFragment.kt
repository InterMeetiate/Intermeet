package com.intermeet.android

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ListView
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }

    // Define your UI elements
    private lateinit var listView: ListView
    private lateinit var searchEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialize UI elements t)
        searchEditText = view.findViewById(R.id.searchbox)
        listView = view.findViewById(R.id.usersList)

        // Set up your list view adapter and other UI interactions here
        val currentUser = getCurrentUserId()
        if (currentUser != null) {
            fetchLikedUsers(currentUser) { users ->
                val adapter = ChatAdapter(requireContext(), users) { userId ->
                    // Handle item click here
                    startChatWithUser(userId)
                }
                listView.adapter = adapter
            }
        }
        // Set item click listener for list view
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val userId = listView.getItemAtPosition(position) as String
            startChatWithUser(userId)
        }

        return view
    }
    private fun startChatWithUser(userId: String) {
        // Start a new chat activity with the user identified by userId
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
    private fun getCurrentUserId(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid
    }

    private fun fetchLikedUsers(userID: String, callback: (List<String>) -> Unit) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("likes")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likedUserIds = snapshot.children.mapNotNull { it.key }
                callback(likedUserIds)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchLikedUsers", "Error fetching liked user IDs: ${error.message}")
                callback(emptyList()) // Return an empty list in case of error
            }
        })
    }

    // You can add more methods/functions here as needed
}