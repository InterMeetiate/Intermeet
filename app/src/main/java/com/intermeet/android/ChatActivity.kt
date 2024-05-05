package com.intermeet.android
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ChatActivity : AppCompatActivity() {

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Retrieve the userId extra from the Intent
        userId = intent.getStringExtra("userId") ?: ""

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the back button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        // Fetch the user's name and set it as the toolbar title
        //fetchUserName(userId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toolbar item clicks
        when (item.itemId) {
            android.R.id.home -> {
                // Respond to the back button click
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            // Add more cases for other toolbar items if needed
        }
        return super.onOptionsItemSelected(item)
    }

    /*private fun fetchUserName(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(ChatAdapter.User::class.java)
                user?.let {
                    // Set the user's name as the toolbar title
                    supportActionBar?.title = "${user.firstName} ${user.lastName}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }*/
}