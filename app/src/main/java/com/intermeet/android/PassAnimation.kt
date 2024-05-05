package com.intermeet.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.AccelerateInterpolator
import android.view.animation.AccelerateDecelerateInterpolator


class PassAnimation : Fragment() {
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var animationDrawable: AnimationDrawable
    private lateinit var passImageView: ImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pass_animation, container, false)
        passImageView = view.findViewById(R.id.pass)
        constraintLayout = view.findViewById(R.id.passanimation)
        constraintLayout.setBackgroundResource(android.R.color.transparent)
        return view

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
                duration = 1000
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

    fun animatePass() {
        // Make the ImageView visible
        passImageView.visibility = View.VISIBLE

        // Define the animation properties
        val scaleX = ObjectAnimator.ofFloat(passImageView, View.SCALE_X, 0.2f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(passImageView, View.SCALE_Y, 0.2f, 1.0f)
        val rotation = ObjectAnimator.ofFloat(passImageView, View.ROTATION, -135f, 0f)

        // Set the interpolator for a smoother animation
        scaleX.interpolator = android.view.animation.AccelerateInterpolator()
        scaleY.interpolator = android.view.animation.AccelerateInterpolator()
        rotation.interpolator = android.view.animation.AccelerateInterpolator()

        // Combine the initial animations into a set
        val initialAnimatorSet = AnimatorSet()
        initialAnimatorSet.playTogether(scaleX, scaleY, rotation)
        initialAnimatorSet.duration = 500 // Set the duration of the initial animation in milliseconds

        // Define the overrotation animation
        val overRotation = ObjectAnimator.ofFloat(passImageView, View.ROTATION, 15f)

        // Define the final rotation animation to return to the regular position
        val finalRotation = ObjectAnimator.ofFloat(passImageView, View.ROTATION, 0f)

        // Set the interpolator for the overrotation animation
        overRotation.interpolator = AccelerateDecelerateInterpolator()

        // Combine the overrotation and final rotation animations into a set
        val finalRotationAnimatorSet = AnimatorSet()
        finalRotationAnimatorSet.playSequentially(overRotation, finalRotation)
        finalRotationAnimatorSet.duration = 200 // Set the duration of the final rotation animation in milliseconds

        // Combine the initial animation and final rotation animations into a set
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(initialAnimatorSet, finalRotationAnimatorSet)
        animatorSet.start()
    }

}