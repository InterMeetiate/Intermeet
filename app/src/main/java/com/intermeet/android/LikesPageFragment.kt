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
import androidx.viewpager2.widget.ViewPager2
import com.intermeet.android.DiscoverActivity
//import com.intermeet.android.DiscoverFragment
import com.intermeet.android.R
import com.intermeet.android.UserDetailAdapter
import com.intermeet.android.UserDetailFragment
import com.intermeet.android.LikesDetailFragment

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


        // Clear the list before adding items to avoid duplicates
        recyclerDataArrayList.clear() //NEED TO CHANGE WAY OF CLEARING IT
        // added data to array list
        recyclerDataArrayList.add("Xqi01fgXQNMdzdQEQEVJ6iB4wDu2")
        recyclerDataArrayList.add("CPzvj0777RUav4ACjiiS6BECxm82")
        recyclerDataArrayList.add("DK6LQRJYmxarqxOHCdy4MKS63Pp2")

        // added data from arraylist to adapter class.
        val adapter = RecyclerViewAdapter(recyclerDataArrayList, this, DiscoverActivity())

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
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

    companion object {
        @JvmStatic
        fun newInstance() = LikesPageFragment()
    }
}