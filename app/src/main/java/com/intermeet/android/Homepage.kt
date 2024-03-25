package com.intermeet.android

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.*

// IMPLEMENT A WAY TO FIGURE OUT CURRENT USER

data class UserData(
    val photoDownloadUrls: List<String>? = null
)
class Homepage : AppCompatActivity() {
    private lateinit var photoViews: List<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)
        photoViews = listOf(
            findViewById(R.id.photo1),
            findViewById(R.id.photo2),
            findViewById(R.id.photo3),
            findViewById(R.id.photo4),
            findViewById(R.id.photo5)
        )

        val userId = "knIJTTeOHsa3ce4L84dbE7BUYQI2"
        val database = Firebase.database
        val userRef = database.getReference("users").child(userId)

        // Add a listener to fetch user data and load images
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(UserData::class.java)
                userData?.photoDownloadUrls?.let { urls ->
                    loadImages(urls)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun loadImages(urls: List<String>) {
        for ((index, url) in urls.withIndex()) {
            if (index < photoViews.size) {
                Glide.with(this)
                    .load(url)
                    .into(photoViews[index])
            }
        }
    }
}


