

//import com.intermeet.android.DiscoverFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.intermeet.android.DiscoverActivity
import com.intermeet.android.LikesDetailFragment
import com.intermeet.android.R
import com.intermeet.android.UserDetailAdapter

class LikesPageFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val recyclerDataArrayList = ArrayList<String>()
    private lateinit var userDetailAdapter: UserDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_likespage, container, false)
        recyclerView = view.findViewById(R.id.idCourseRV)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        //val database = Firebase.database
        //val userRef = database.getReference("users").child(userId.toString())

        // Clear the list before adding items to avoid duplicates
        /*if(recyclerDataArrayList.size > 0)
        {//NEED TO CHANGE WAY OF CLEARING IT
            for(i in recyclerDataArrayList)
            {
                recyclerDataArrayList.remove(i)
            }
        }*/
        // added data to array list
        //recyclerDataArrayList.add("Xqi01fgXQNMdzdQEQEVJ6iB4wDu2")
        //recyclerDataArrayList.add("CPzvj0777RUav4ACjiiS6BECxm82")
        //recyclerDataArrayList.add("DK6LQRJYmxarqxOHCdy4MKS63Pp2")
        //userRef.addValueEventListener()
        if (userId != null) {
            fetchLikedUsers(userId) {
                users -> recyclerDataArrayList
            }
        }

        // added data from arraylist to adapter class.
        val adapter = RecyclerViewAdapter(recyclerDataArrayList, this, DiscoverActivity())

        // setting grid layout manager to implement grid view.
        // in this method '2' represent/s number of columns to be displayed in grid view.
        val layoutManager = GridLayoutManager(requireContext().applicationContext, 2)

        // at last set adapter to recycler view.
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        userDetailAdapter = UserDetailAdapter(this)


        adapter.setOnClickListener(object : RecyclerViewAdapter.OnClickDetect {
            override fun onClickDetect(position: Int, userId: String) {
                val likesDetailFragment = LikesDetailFragment.newInstance(userId)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, likesDetailFragment)
                    .addToBackStack(null)  // Optional: Add transaction to the back stack
                    .commit()
            }
        })

        return view
    }

    private fun fetchLikedUsers(userID: String, callback: (List<String>) -> List<String>) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("likes")
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likedUserIds = snapshot.children.mapNotNull { it.key }
                callback(likedUserIds)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchLikedUsers", "Error fetching liked user IDs: ${error.message}")
                callback(emptyList()) // Return an empty list in case of error
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = LikesPageFragment()
    }
}