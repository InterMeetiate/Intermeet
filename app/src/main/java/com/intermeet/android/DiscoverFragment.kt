import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
    private lateinit var noUsersTextView: TextView
    private lateinit var btnRefresh: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLike: Button = view.findViewById(R.id.btnLike)
        val btnPass: Button = view.findViewById(R.id.btnPass)
        val returnButton: View = view.findViewById(R.id.return_button)
        btnRefresh = view.findViewById(R.id.btnRefresh)

        viewPager = view.findViewById(R.id.usersViewPager)
        viewPager.isUserInputEnabled = false
        adapter = UsersPagerAdapter(this)
        viewPager.adapter = adapter
        noUsersTextView = view.findViewById(R.id.tvNoUsers)

        // Marking user as seen
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val userId = adapter.getUserId(position)
                viewModel.markAsSeen(userId)
            }
        })




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

        btnRefresh.setOnClickListener {
            viewModel.clearSeenUsers()
            if (isAdded) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, newInstance())
                    .commit()
            }
        }

        // Observe the filtered user IDs LiveData
        viewModel.filteredUserIdsLiveData.observe(viewLifecycleOwner) { userIds ->
            if (userIds.isNotEmpty()) {
                noUsersTextView.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
                btnRefresh.visibility = View.GONE
                adapter.setUserIds(userIds)
                adapter.notifyDataSetChanged()
                viewPager.currentItem = 0

                // Log the filtered user IDs
                Log.d("DiscoverFragment", "Filtered User IDs: $userIds")
            } else {
                noUsersTextView.visibility = View.VISIBLE
                viewPager.visibility = View.GONE
                btnRefresh.visibility = View.VISIBLE

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
        } else {
            noUsersTextView.visibility = View.VISIBLE
            viewPager.visibility = View.GONE
            btnRefresh.visibility = View.VISIBLE
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
