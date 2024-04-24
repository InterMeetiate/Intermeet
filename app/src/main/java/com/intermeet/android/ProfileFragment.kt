package com.intermeet.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    companion object {
        private const val TAG = "ProfileFragment"
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private lateinit var ivUserProfilePhoto: ImageView
    private lateinit var tvUserFirstName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivUserProfilePhoto = view.findViewById(R.id.ivUserProfilePhoto)
        tvUserFirstName = view.findViewById(R.id.tvUserFirstName)



        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = Firebase.database
        val userRef = database.getReference("users").child(userId)

        // ValueEventListener to read the "firstName" and "photoDownloadURLs" data
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the value of "firstName"
                val userData = dataSnapshot.getValue(UserData::class.java)

                userData?.let { user ->
                    // Setting firstName and calculating age
                    val firstName = user.firstName
                    val birthday = user.birthday
                    val age = calculateAge(birthday)
                    tvUserFirstName.text = "$firstName, $age"

                    // Setting profile photo if available
                    user.photoDownloadUrls?.firstOrNull()?.let { url ->
                        Glide.with(this@ProfileFragment)
                            .load(url)
                            .circleCrop()
                            .into(ivUserProfilePhoto)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log any errors
                Log.w(TAG, "loadUserData:onCancelled", databaseError.toException())
                // Handle error case, perhaps set TextView to an error message
                tvUserFirstName.text = getString(R.string.error_loading_data)
            }
        })

        val settingsButton: Button = view.findViewById(R.id.settingButton)
        settingsButton.setOnClickListener {
            // Start an Intent to open the SettingsActivity
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }
        val editPreference : Button = view.findViewById(R.id.preferenceButton)
        editPreference.setOnClickListener {
            // In the calling Activity
            val intent = Intent(activity, EditPreference::class.java).apply {
                putExtra("isEditMode", true) // true if editing, false or absent if signing up
            }
            startActivity(intent)
        }
        val editUserInfo : Button = view.findViewById(R.id.editProfileButton)
        editUserInfo.setOnClickListener {
            // In the calling Activity
            val intent = Intent(activity, EditProfile::class.java).apply {
                putExtra("isEditMode", true) // true if editing, false or absent if signing up
            }
            startActivity(intent)
        }

    }

    private fun navigateToSettings() {
        // Implementation depends on your navigation setup.
        // This could be using findNavController().navigate() if using Navigation Component
        // or activity supportFragmentManager for manual transactions
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "${this::class.java.simpleName} resumed")
        Log.d("NavigationStatus", "${this::class.java.simpleName} is now visible")

        //view?.findViewById<View>(R.id.main_content)?.visibility = View.VISIBLE
        //view?.findViewById<View>(R.id.nested_nav_host_fragment)?.visibility = View.GONE
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "${this::class.java.simpleName} paused")
    }


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
