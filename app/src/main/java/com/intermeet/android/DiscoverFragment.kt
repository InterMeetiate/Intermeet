import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.intermeet.android.CardStackAdapter
import com.intermeet.android.DiscoverViewModel
import com.intermeet.android.R
import com.intermeet.android.UserDataModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class DiscoverFragment : Fragment() {
    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var cardStackView: CardStackView
    private lateinit var adapter: CardStackAdapter
    private lateinit var noUsersTextView: TextView
    private lateinit var btnRefresh: Button
    private lateinit var returnButton: View
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupListeners()
        setupCardStackView()

        viewModel.filteredUsers.observe(viewLifecycleOwner) { users ->
            progressBar.visibility = View.GONE
            if (users.isNotEmpty()) {
                updateAdapter(users)
            } else {
                displayNoUsers()
            }
        }

        fetchUsers()
    }


    private fun setupViews(view: View) {
        cardStackView = view.findViewById(R.id.usersCardStackView)
        noUsersTextView = view.findViewById(R.id.tvNoUsers)
        btnRefresh = view.findViewById(R.id.btnRefresh)
        returnButton = view.findViewById(R.id.return_button)
        progressBar = view.findViewById(R.id.loadingProgressBar)

        adapter = CardStackAdapter(requireContext(), listOf())
        cardStackView.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() {
        returnButton.setOnClickListener {
        }

        btnRefresh.setOnClickListener {
            fetchUsers()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchUsers() {
        progressBar.visibility = View.VISIBLE
        noUsersTextView.visibility = View.GONE
        viewModel.clearSeenUsers()
        viewModel.fetchAndFilterUsers()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayNoUsers() {
        noUsersTextView.visibility = View.VISIBLE
        btnRefresh.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAdapter(users: List<UserDataModel>) {
        adapter.setUsers(users)
        if (users.isEmpty()) {
            displayNoUsers()
        } else {
            cardStackView.visibility = View.VISIBLE
            noUsersTextView.visibility = View.GONE
        }
    }

    private fun setupCardStackView() {
        val manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                // You can leave this empty or add some logging if you want
            }

            override fun onCardSwiped(direction: Direction) {
                // Handle card swipe if necessary
            }

            override fun onCardRewound() {
                // Handle rewind if necessary
            }

            override fun onCardCanceled() {
                // Handle card cancel if necessary
            }

            override fun onCardAppeared(view: View, position: Int) {
                // Fade in the card as it appears
                view.animate().alpha(1.0f).setDuration(300).start()
            }

            override fun onCardDisappeared(view: View, position: Int) {
            }
        }).apply {
            setDirections(Direction.HORIZONTAL)
            setCanScrollVertical(false)
            setCanScrollHorizontal(true)
        }


        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
    }


    companion object {
        fun newInstance() = DiscoverFragment()
    }
}