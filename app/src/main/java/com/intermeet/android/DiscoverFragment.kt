import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.intermeet.android.DiscoverViewModel
import com.intermeet.android.R
import com.intermeet.android.UsersPagerAdapter


class DiscoverFragment : Fragment() {
    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: UsersPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.usersViewPager)
        viewPager.isUserInputEnabled = false
        adapter = UsersPagerAdapter(this)
        viewPager.adapter = adapter

        // Marking user as seen
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val userId = adapter.getUserId(position)
                viewModel.markAsSeen(userId)
            }
        })


        val btnLike: Button = view.findViewById(R.id.btnLike)
        val btnPass: Button = view.findViewById(R.id.btnPass)
        val returnButton: View = view.findViewById(R.id.return_button)

        btnLike.setOnClickListener {
            val likedUserId = adapter.getUserId(viewPager.currentItem)
            viewModel.addLike(likedUserId)
            navigateToNextUser()
        }

        btnPass.setOnClickListener {
            navigateToNextUser()
        }
        returnButton.setOnClickListener {
            navigateToPreviousUser()
        }

        // Observe the filtered user IDs LiveData
        viewModel.filteredUserIdsLiveData.observe(viewLifecycleOwner) { userIds ->
            if (userIds.isNotEmpty()) {
                adapter.setUserIds(userIds)
                adapter.notifyDataSetChanged()
                // Log the filtered user IDs
                Log.d("DiscoverFragment", "Filtered User IDs: $userIds")
            } else {
                // Log when no user IDs are available
                Log.d("DiscoverFragment", "No filtered user IDs available")
            }
        }
        // Trigger the fetching and filtering of users
        viewModel.fetchAndFilterUsers()
    }


    private fun navigateToNextUser() {
        if (viewPager.currentItem < adapter.itemCount - 1) {
            viewPager.currentItem += 1
        }
    }

    private fun navigateToPreviousUser() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem -= 1
        }
    }


    companion object {
        fun newInstance() = DiscoverFragment()
    }
}
