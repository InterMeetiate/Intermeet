package com.intermeet.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MatchAnimationTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_animation)

        val button: Button = findViewById(R.id.button)
        val button2: Button = findViewById(R.id.likebutton)

        // Ensure the fragment is properly managed
        var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as MatchAnimation?
        if (fragment == null) {
            fragment = MatchAnimation()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }
        var fragment2 = supportFragmentManager.findFragmentById(R.id.fragmentContainer2) as LikeAnimation?
        if (fragment2 == null){
            fragment2 = LikeAnimation()  // Use '=' for assignment
            if (fragment2 != null) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer2, fragment2)
                    .commit()
            }
        }


        button.setOnClickListener {
            fragment.loadImages("3MuNR6f5DJZXYtpe92nq89LgCCV2", "ASpSWWVctpdsCYZPfxOdTmSJ4e72")
            fragment.toggleBackgroundAnimation()
            fragment.toggleHeartVisibility()
            //fragment.toggleCurves()
        }
        button2.setOnClickListener{
            fragment2.toggleBackgroundAnimation()
            fragment2.animateLike()
        }

    }
}
