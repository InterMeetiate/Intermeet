package com.intermeet.android

import UserCardStackAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yuyakaido.android.cardstackview.*
import com.intermeet.android.R

class UserDetailFragment : Fragment(), CardStackListener {

    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var cardStackView: CardStackView
    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var adapter: UserCardStackAdapter

    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString("ARG_USER_ID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardStackView = view.findViewById(R.id.cardStackView)
        cardStackLayoutManager = CardStackLayoutManager(requireContext(), this).apply {
            setStackFrom(StackFrom.Top) // Start stacking from the top
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual) // Allow both manual and automatic swiping
            setDirections(listOf(Direction.Left, Direction.Right)) // Allow swiping left and right
        }
        cardStackView.layoutManager = cardStackLayoutManager

        adapter = UserCardStackAdapter(emptyList())
        cardStackView.adapter = adapter

        userId?.let { id ->
            viewModel.fetchUserData(id)
            viewModel.userData.observe(viewLifecycleOwner) { userData ->
                if (userData != null) {
                    adapter.updateData(listOf(userData)) // Update adapter with fetched user data
                }
            }
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            // Handle like action
        } else if (direction == Direction.Left) {
            // Handle pass action
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        // Optional: implement card dragging behavior
    }

    override fun onCardRewound() {
        // Optional: implement card rewinding behavior
    }

    override fun onCardAppeared(view: View?, position: Int) {
        // Optional: handle card appearance
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        // Optional: handle card disappearance
    }

    override fun onCardCanceled() {
        // Optional: handle card cancelation
    }

    companion object {
        fun newInstance(userId: String): UserDetailFragment {
            val fragment = UserDetailFragment()
            val args = Bundle().apply {
                putString("ARG_USER_ID", userId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}