package com.intermeet.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "ProfileActivity"
    }

    private lateinit var tvDrinkingStatus: TextView
    private lateinit var ivUserProfilePhoto: ImageView
    private lateinit var tvUserFirstName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val userId = "knIJTTeOHsa3ce4L84dbE7BUYQI2"
        val database = Firebase.database

        // Initialize TextViews and ImageView
        tvDrinkingStatus = findViewById(R.id.tvDrinkingStatus)
        ivUserProfilePhoto = findViewById(R.id.ivUserProfilePhoto)
        tvUserFirstName = findViewById(R.id.tvUserFirstName)

        // Reference to the user's "drinking" and "firstName" fields
        val userDrinkingRef = database.getReference("users").child(userId).child("drinking")
        val userNameRef = database.getReference("users").child(userId).child("firstName")

        // Reference to the user's "photoDownloadURLs" node
        val userPhotosRef = database.getReference("users").child(userId).child("photoDownloadURLs")

        // ValueEventListener to read the "drinking" data
        userDrinkingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the value of "drinking"
                val drinkingValue = dataSnapshot.getValue<String>()
                // Set the value of "drinking" to the TextView
                tvDrinkingStatus.text = "Drinking: $drinkingValue"
                Log.d(TAG, "Drinking: $drinkingValue")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log any errors
                Log.w(TAG, "loadUserDrinking:onCancelled", databaseError.toException())
                // Handle error case, perhaps set TextView to an error message
                tvDrinkingStatus.text = getString(R.string.error_loading_data)
            }
        })

        // ValueEventListener to read the "firstName" data
        userNameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the value of "firstName"
                val nameValue = dataSnapshot.getValue<String>()
                // Set the value of "firstName" to the TextView
                tvUserFirstName.text = nameValue
                Log.d(TAG, "Name: $nameValue")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log any errors
                Log.w(TAG, "loadUserName:onCancelled", databaseError.toException())
                // Handle error case, perhaps set TextView to an error message
                tvUserFirstName.text = getString(R.string.error_loading_data)
            }
        })

        // ValueEventListener to read the "photoDownloadURLs" data
        // ValueEventListener to read the "photoDownloadURLs" data
        userPhotosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the dataSnapshot has children
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    // Get the first photo URL value
                    val photoUrl = dataSnapshot.children.firstOrNull()?.getValue<String>()
                    photoUrl?.let {
                        // Use Glide to load the photo into the ImageView
                        Glide.with(this@ProfileActivity)
                            .load(it)
                            .into(ivUserProfilePhoto)
                    } ?: run {
                        Log.w(TAG, "No photos found at the specified path.")
                    }
                } else {
                    // Handle the case where there are no photos
                    Log.w(TAG, "No photos found at the specified path.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log any errors
                Log.w(TAG, "loadUserPhotos:onCancelled", databaseError.toException())
            }
        })

    }
}
