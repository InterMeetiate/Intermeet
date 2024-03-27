package com.intermeet.android.Home_Page

import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.intermeet.android.R

class HomePage_Test2 : AppCompatActivity(){
    private lateinit var hiddenView: LinearLayout
    private lateinit var cardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_main)

        cardView = findViewById(R.id.base_cardview)
        //arrow = findViewById(R.id.arrow_button)
        hiddenView = findViewById(R.id.hidden_view)
    }

    public fun expandDisplay(view: View)
    {
        // If the CardView is already expanded, set its visibility
        // to gone and change the expand less icon to expand more.
        if (hiddenView.visibility == View.VISIBLE) {
            // The transition of the hiddenView is carried out by the TransitionManager class.
            // Here we use an object of the AutoTransition Class to create a default transition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
            }
            hiddenView.visibility = View.GONE
            //arrow.setImageResource(R.drawable.location_icon)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(cardView, AutoTransition())
            }
            hiddenView.visibility = View.VISIBLE
            //arrow.setImageResource(R.drawable.location_icon)
        }
    }
}