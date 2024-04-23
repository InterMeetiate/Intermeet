package com.intermeet.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.GridView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intermeet.android.R

class LikesPageFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val recyclerDataArrayList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_likespage, container, false)
        recyclerView = view.findViewById(R.id.idCourseRV)

        // added data to array list
//        recyclerDataArrayList.add(RecyclerData("DSA", R.drawable.ic_gfglogo))
//        recyclerDataArrayList.add(RecyclerData("JAVA", R.drawable.ic_gfglogo))
        recyclerDataArrayList.add("Xqi01fgXQNMdzdQEQEVJ6iB4wDu2")
        recyclerDataArrayList.add("XfoEflW182Rt4VG5uLqAJea99uC2")

        // added data from arraylist to adapter class.
        val adapter = RecyclerViewAdapter(recyclerDataArrayList, this)

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        val layoutManager = GridLayoutManager(activity, 2)

        // at last set adapter to recycler view.
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        return view
    }

    /*private fun getFirstImage(userId : String)
    {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users/$userId")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) : String? {
                val userReference = snapshot.getValue(UserData::class.java)
                val likesImage = findViewById<ImageView>(R.id.likespage_image)
                if(likesImage != null)
                {
                    return userReference?.photoDownloadUrls?.firstOrNull()
                    *//*userReference?.photoDownloadUrls?.firstOrNull()?.let { url ->
                        Glide.with(this@LikesPageFragment)
                            .load(url)
                            .into(likesImage)
                    }*//*
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }*/

    companion object {
        @JvmStatic
        fun newInstance() = LikesPageFragment()
    }
}

