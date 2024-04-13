import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.intermeet.android.DiscoverViewModel
import com.intermeet.android.R
import com.intermeet.android.UserDataModel
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

        val btnLike: Button = view.findViewById(R.id.btnLike)
        val btnPass: Button = view.findViewById(R.id.btnPass)

        btnLike.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }

        btnPass.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            }
        }

        viewModel.nearbyUserIdsLiveData.observe(viewLifecycleOwner) { userIds ->
            if (userIds.isNotEmpty()) {
                adapter.setUserIds(userIds)
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.fetchCurrentUserLocationAndQueryNearbyUsers()
    }


    companion object {
        fun newInstance() = DiscoverFragment()
    }
}
