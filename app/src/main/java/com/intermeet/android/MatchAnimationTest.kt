package com.intermeet.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class MatchAnimationTest : AppCompatActivity() {

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_animation)


        button = findViewById(R.id.button)

        // Assume a valid user ID is available
        val userId = "3MuNR6f5DJZXYtpe92nq89LgCCV2"
        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            loadUserData("3MuNR6f5DJZXYtpe92nq89LgCCV2", imageView1)
            loadUserData("ASpSWWVctpdsCYZPfxOdTmSJ4e72", imageView2)
        }
    }

    private fun loadUserData(userId: String, imageView: ImageView) {;
        val database = FirebaseDatabase.getInstance().getReference("users").child(userId)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue<UserData>()

                if (userData?.photoDownloadUrls.isNullOrEmpty()) {
                    Log.w("MatchAnimationTest", "No URLs found or user data is null")
                    // Optionally update the UI to inform the user
                } else {
                    if (userData != null) {
                        userData.photoDownloadUrls?.firstOrNull()?.let { url ->
                            Glide.with(this@MatchAnimationTest)
                                .load(url)
                                .circleCrop()

                                .into(imageView)

                            // Start fade-in animation
                            imageView.alpha = 0f
                            val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
                            fadeIn.duration = 500 // duration of 1 second
                            fadeIn.start()

                            // Set the image to fade out after 5 seconds
                            Handler(Looper.getMainLooper()).postDelayed({
                                val fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
                                fadeOut.duration = 500 // duration of 1 second
                                fadeOut.start()
                                fadeOut.addListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        imageView.setImageDrawable(null) // Clear the image after fade out
                                    }
                                })
                            }, 1500) // Display the image for 5 seconds before starting fade out
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("MatchAnimationTest", "loadUserData:onCancelled", databaseError.toException())
                // Optionally update the UI to inform the user of the error
            }
        })
    }


}
