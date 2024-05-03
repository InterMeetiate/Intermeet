package com.intermeet.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue



class MatchAnimation : Fragment() {

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var animationDrawable: AnimationDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match_animation, container, false)
        imageView1 = view.findViewById(R.id.imageView1)
        imageView2 = view.findViewById(R.id.imageView2)
        constraintLayout = view.findViewById(R.id.matchanimation)
        constraintLayout.setBackgroundResource(android.R.color.transparent)
        return view
    }

    fun loadImages(userId1: String, userId2: String) {
        loadUserData(userId1, imageView1)
        loadUserData(userId2, imageView2)
    }

    private fun loadUserData(userId: String, imageView: ImageView) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(userId)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue<UserData>()

                if (userData?.photoDownloadUrls.isNullOrEmpty()) {
                    Log.w("ImageDisplayFragment", "No URLs found or user data is null")
                } else {
                    if (userData != null) {
                        userData.photoDownloadUrls?.firstOrNull()?.let { url ->
                            Glide.with(this@MatchAnimation)
                                .load(url)
                                .circleCrop()
                                .into(imageView)

                            // Start fade-in animation
                            imageView.alpha = 0f
                            val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f)
                            fadeIn.duration = 500
                            fadeIn.start()

                            // Set the image to fade out after 5 seconds
                            Handler(Looper.getMainLooper()).postDelayed({
                                val fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f)
                                fadeOut.duration = 700
                                fadeOut.start()
                                fadeOut.addListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        imageView.setImageDrawable(null) // Clear the image after fade out
                                    }
                                })
                            }, 1500)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ImageDisplayFragment", "loadUserData:onCancelled", databaseError.toException())
            }
        })
    }
    fun toggleBackgroundAnimation() {
        constraintLayout.setBackgroundResource(R.drawable.gradient_list)
        animationDrawable = constraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(500)
        animationDrawable.setExitFadeDuration(700)

        // Start the animation
        animationDrawable.start()

        // Fade in the background
        ObjectAnimator.ofFloat(constraintLayout, "alpha", 0f, 1f).apply {
            duration = 500
            start()
        }

        // Delay to view the gradient, then fade out
        Handler(Looper.getMainLooper()).postDelayed({
            ObjectAnimator.ofFloat(constraintLayout, "alpha", 1f, 0f).apply {
                duration = 700
                start()
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // Set back to transparent to avoid drawing over other elements when not in use
                        constraintLayout.setBackgroundResource(android.R.color.transparent)
                    }
                })
            }
        }, 1500) // Total visible duration of the gradient background
    }

    data class UserData(
        val firstName: String? = null,
        val photoDownloadUrls: List<String>? = null
    )
}
