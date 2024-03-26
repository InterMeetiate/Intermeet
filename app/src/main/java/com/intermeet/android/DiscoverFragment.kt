package com.intermeet.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.intermeet.android.helperFunc.calculateAgeWithCalendar

class DiscoverFragment : Fragment() {

    private lateinit var textViewName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        textViewName = view.findViewById(R.id.textViewName)

        fetchDataFromFirebase()

        return view
    }

    private fun fetchDataFromFirebase() {
        val userId = "a6TWxI1076ahgLhZFOaHmNPRbom2" // Replace with dynamic user ID as needed
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserDataModel::class.java)
                user?.let {
                    textViewName.text = "${it.firstName}, ${calculateAgeWithCalendar(it.birthday)}"
                    // Assuming 'birthdate' is a field in UserDataModel and is in a format parseable by calculateAgeWithCalendar.

                    val imageViews = listOf(
                        view?.findViewById<ImageView>(R.id.imageView1),
                        view?.findViewById<ImageView>(R.id.imageView2),
                    )

                    imageViews.forEachIndexed { index, imageView ->
                        if (index < it.photoDownloadUrls.size) {
                            imageView?.let { imgView ->
                                Glide.with(this@DiscoverFragment)
                                    .load(it.photoDownloadUrls[index])
                                    .centerCrop()
                                    .into(imgView)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = DiscoverFragment()
    }
}
