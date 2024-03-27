package com.intermeet.android

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.*

// IMPLEMENT A WAY TO FIGURE OUT CURRENT USER

data class UserData(
    val firstName: String? = null,
    val birthday: String? = null,
    val photoDownloadUrls: List<String>? = null
)
class Homepage : AppCompatActivity() {
    private lateinit var photoViews: List<ImageView>
    private lateinit var userNameAge: TextView
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
        userNameAge = findViewById(R.id.userNameAge)

        val userId = "knIJTTeOHsa3ce4L84dbE7BUYQI2"
        val database = Firebase.database
        val userRef = database.getReference("users").child(userId)

        // Add a listener to fetch user data and load images
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userData = dataSnapshot.getValue(UserData::class.java)

                userData?.let { user ->
                    val firstName = user.firstName
                    val birthday = user.birthday
                    val age = calculateAge(birthday)

                    userNameAge.text = "$firstName, $age"

                    userData?.photoDownloadUrls?.let { urls ->
                        loadImages(urls)
                    }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateAge(birthday: String?): Int {
        // Implement logic to calculate age based on birthday
        // Example: Parse birthday string, calculate age based on current date, and return age
        // For brevity, a simplified implementation is shown here:
        // Note: You may need to handle date parsing and calculation more accurately in a real app.

        if (birthday.isNullOrEmpty()) return 0 // Default age if birthday is not provided
        var day: Int? = null
        var month: Int? = null
        var year: Int? = null
        val parts = birthday.split("-")

        if (parts.size == 3) {
            day = parts[0].toIntOrNull()
            month = parts[1].toIntOrNull()
            year = parts[2].toIntOrNull()

            if (day != null && month != null && year != null) {
                println("Day: $day")
                println("Month: $month")
                println("Year: $year")
            }
        }

        val currentYear = java.time.LocalDate.now().year
        val currentMonth = java.time.LocalDate.now().monthValue
        val currentDay = java.time.LocalDate.now().dayOfMonth
        var currentAge = currentYear - year!!
        if(currentMonth < month!!) {
            currentAge -= 1
        }
        else if(currentMonth == month!!) {
            if(currentDay < day!!) {
                currentAge -= 1
            }
        }

        return currentAge
    }
}

